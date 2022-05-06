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

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Callback helper methods
 */
public final class Callbacks {
    private static final Logger LOG = LoggerFactory.getLogger(Callbacks.class);

    private Callbacks() { }

    /**
     * For every element, invoke the given callback.
     * Stops if {@link CallbackRefusedException} is thrown.
     * @param callback the callback to call for each item
     * @param items item(s) to invoke the callback with
     */
    @SafeVarargs
    public static <T> void stream(Callback<T> callback, T... items) throws Exception
    {
        stream(callback, Arrays.asList(items));
    }

    /**
     * For every element in the iterable, invoke the given callback.
     * Stops if {@link CallbackRefusedException} is thrown.
     * @param callback the callback to invoke for each item
     * @param iterable the list of ietms to invoke the callback with
     */
    public static <T> void stream(Callback<T> callback, Iterable<T> iterable) throws Exception
    {
        for (T item : iterable) {
            try {
                callback.call(item);
            } catch (CallbackRefusedException e) {
                LOG.trace("callback refused", e);
                return;
            }
        }
    }

    /**
     * A callback that does nothing.
     */
    @SuppressWarnings("unchecked")
    public static <T> Callback<T> noop()
    {
        return (Callback<T>) NOOP;
    }

    /**
     * A callback that does nothing.
     */
    public static final Callback<Object> NOOP = new NoopCallback();

    /**
     * A callback that does nothing
     */
    private static class NoopCallback implements Callback<Object>
    {
        @Override
        public void call(Object item) throws Exception { }
    }

    /**
     * Combine multiple callbacks into a single callback, preserving order.
     * @param callbacks the callbacks to combine into a single callback
     */
    @SafeVarargs
    public static <T> Callback<T> chain(Callback<T>... callbacks)
    {
        return chain(Arrays.asList(callbacks));
    }

    /**
     * Combine multiple callbacks into a single callback, preserving order.
     */
    public static <T> Callback<T> chain(Iterable<Callback<T>> callbacks)
    {
        return new ChainCallback<T>(callbacks);
    }

    /**
     * A callback that executes a chain of callbacks
     *
     * @param <T> the type of item the callback processes
     */
    private static class ChainCallback<T> implements Callback<T>
    {
        private final Iterable<Callback<T>> callbacks;

        /**
         * Create a chain callback
         * @param callbacks the callbacks to call in order
         */
        ChainCallback(Iterable<Callback<T>> callbacks)
        {
            this.callbacks = callbacks;
        }

        @Override
        public void call(T item) throws Exception
        {
            for (Callback<T> callback : callbacks) {
                callback.call(item);
            }
        }
    }
}
