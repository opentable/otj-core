/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opentable.callback;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A callback that puts submitted items into a batch. When the batch is committed it is processed via the supplied callback via the provided executor.
 * A commit happens when the number of items reaches the given batch size, or when commit is called, or when the callback is closed.
 *
 * @param <T> the type of item to process
 */
class ExecutorBatchingCallback<T> extends BatchingCallback<T>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorBatchingCallback.class);

    /**
     * Create an executor batching callback. It executes the callback on items whenever a batch is committed (see {@link BatchingCallback}).
     * @param size how many items should be collected into a batch before committing
     * @param executor the executor with which to execute the callback
     * @param out the callback to process batches of items
     * @param failFast whether the first error encountered should stop additional processing of items
     */
    ExecutorBatchingCallback(int size, ExecutorService executor, Callback<? super List<T>> out, boolean failFast)
    {
        super(size, new ExecutorCallback<>(executor, out, failFast));
    }

    @Override
    public boolean commit()
    {
        boolean result = super.commit();
        ExecutorCallback.class.cast(getOut()).close();
        return result;
    }

    /**
     * A callback that wraps another callback and executes it with an executor service.
     * Can be set to fail fast on the first processing exception and not process additional items.
     *
     * @param <T> the type of item to process
     */
    static class ExecutorCallback<T> implements Callback<List<T>>
    {
        private final ExecutorCompletionService<Void> executor;
        private final Callback<? super List<T>> out;
        private final AtomicLong inFlight = new AtomicLong();
        private final BatchingCallbackExecutionException exceptions = new BatchingCallbackExecutionException();
        private final AtomicBoolean failed = new AtomicBoolean();
        private final boolean failFast;

        /**
         * Create an executor callback
         * @param executor the executor to run the callback on
         * @param out the callback to run
         * @param failFast whether the first error encountered should stop additional processing
         */
        ExecutorCallback(ExecutorService executor, Callback<? super List<T>> out, boolean failFast)
        {
            this.executor = new ExecutorCompletionService<>(executor);
            this.out = out;
            this.failFast = failFast;
        }

        @Override
        public void call(final List<T> item) throws Exception
        {
            if (failed.get()) {
                throw new CallbackRefusedException();
            }

            inFlight.incrementAndGet();
            executor.submit(new ExecutorCallable<T>(out, item));

            Future<Void> f;
            while ( (f = executor.poll()) != null ) { //NOPMD
                inFlight.decrementAndGet();
                try {
                    f.get();
                } catch (ExecutionException e) {
                    LOGGER.warn("Callback failed", e);
                    exceptions.addSuppressed(e.getCause());

                    if (failFast) {
                        failed.set(true);
                        exceptions.fillInStackTrace();
                        throw exceptions;
                    }
                }
            }
        }

        /**
         * Let all the in flight requests finish processing
         * Throws an exception when all in flight requests are done if any exceptions were encountered
         */
        public void close()
        {
            while (inFlight.decrementAndGet() >= 0) {
                try {
                    executor.take().get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException(e);
                } catch (ExecutionException e) {
                    exceptions.addSuppressed(e.getCause());
                }
            }

            if (exceptions.getSuppressed().length != 0) {
                exceptions.fillInStackTrace();
                throw exceptions;
            }
        }
    }

    /**
     * The callable to run via the executor
     *
     * @param <T> the type of item to process
     */
    static class ExecutorCallable<T> implements Callable<Void>
    {
        private final Callback<? super List<T>> out;
        private final List<T> item;

        /**
         * Create the executor callable
         * @param out the wrapped callback
         * @param item the list of items to process when called
         */
        ExecutorCallable(Callback<? super List<T>> out, List<T> item)
        {
            this.out = out;
            this.item = item;
        }

        @Override
        public Void call() throws Exception
        {
            out.call(item);
            return null;
        }
    }
}
