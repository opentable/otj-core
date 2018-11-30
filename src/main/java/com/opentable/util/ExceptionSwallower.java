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
public final class ExceptionSwallower
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionSwallower.class);

    private ExceptionSwallower() {
        /* utility class */
    }
    /**
     * Transform a Runnable to log and then swallow any thrown exceptions. However, {@link Error}s are still re-thrown.
     * @param in the runnable to wrap
     * @return a runnable that will swallow exceptions
     */
    @SuppressWarnings({"PMD.AvoidCatchingThrowable","PMD.AvoidInstanceofChecksInCatchClause"})
    public static Runnable swallowExceptions(Runnable in)
    {
        return () -> {
            try {
                in.run();
            } catch (Error tt) {
                LOGGER.error("Error (will be rethrown)", tt);
                throw tt;
            } catch (Throwable t) {
                LOGGER.error("Uncaught exception swallowed", t);
                // Ignore the IDE warning - consider Lombok's SneakyThrows, which is an abomination but heavily used by heathens, whom I pray for.
                if (t instanceof InterruptedException) {
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
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public static <T> Consumer<T> swallowExceptions(ThrowingConsumer<T> in)
    {
        return (item) -> {
            try {
                in.accept(item);
            } catch (InterruptedException e) {
                LOGGER.warn("Interrupted exception", e);
                Thread.currentThread().interrupt();
            } catch (Error tt) {
                LOGGER.error("Error (will be rethrown)", tt);
                throw tt;
            } catch (Throwable t) {
                LOGGER.error("Uncaught exception swallowed", t);
            }
        };
    }

    /**
     * Transform a Function to log and then swallow any thrown exceptions.
     * If an exception is swallowed, return null. However, {@link Error}s are still re-thrown.
     * @param in the function to wrap
     * @return a function that swallows exceptions (logging them and returning null)
     */
    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public static <A, B> Function<A, B> forFunction(ThrowingFunction<A, B> in)
    {
        return (item) -> {
            try {
                return in.apply(item);
            } catch (InterruptedException e) {
                LOGGER.warn("Interrupted exception", e);
                Thread.currentThread().interrupt();
            } catch (Error tt) {
                LOGGER.error("Error (will be rethrown)", tt);
                throw tt;
            } catch (Throwable t) {
                LOGGER.error("Uncaught exception swallowed", t);
            }
            return null;
        };
    }
}
