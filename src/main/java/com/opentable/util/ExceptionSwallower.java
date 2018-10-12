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
package com.opentable.util;

import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opentable.function.ThrowingConsumer;
import com.opentable.function.ThrowingFunction;

/**
 * Transformations for various functional interfaces to modify exceptional behavior.
 */
public class ExceptionSwallower
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionSwallower.class);

    /**
     * Transform a Runnable to log and then swallow any thrown exceptions. However, {@link Error}s are still re-thrown.
     * @param in the runnable to wrap
     * @return a runnable that will swallow exceptions
     */
    public static Runnable swallowExceptions(Runnable in)
    {
        return () -> {
            try {
                in.run();
            } catch (Throwable t) {
                LOGGER.error("Uncaught exception swallowed", t);
                if (t instanceof Error) {
                    throw t;
                } else if (t instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
        };
    }

    /**
     * Transform a Consumer to log and then swallow any thrown exceptions. However, {@link Error}s are still re-thrown.
     * @param in a consumer that may throw exceptions
     * @return a consumer that swallows exceptions
     */
    public static <T> Consumer<T> swallowExceptions(ThrowingConsumer<T> in)
    {
        return (item) -> {
            try {
                in.accept(item);
            } catch (Throwable t) {
                LOGGER.error("Uncaught exception swallowed", t);
                if (t instanceof Error) {
                    throw (Error) t;
                } else if (t instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
        };
    }

    /**
     * Transform a Function to log and then swallow any thrown exceptions.
     * If an exception is swallowed, return null. However, {@link Error}s are still re-thrown.
     * @param in the function to wrap
     * @return a function that swallows exceptions (logging them and returning null)
     */
    public static <A, B> Function<A, B> forFunction(ThrowingFunction<A, B> in)
    {
        return (item) -> {
            try {
                return in.apply(item);
            } catch (Throwable t) {
                LOGGER.error("Uncaught exception swallowed", t);
                if (t instanceof Error) {
                    throw (Error) t;
                } else if (t instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
            return null;
        };
    }
}
