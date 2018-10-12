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

/**
 * A function to execute as a callback
 *
 * @param <T> the type of item the callback processes
 */
@FunctionalInterface
public interface Callback<T>
{
    /**
     * Process a single item.
     *
     * An implementation of this interface can throw {@link CallbackRefusedException} to signal the caller
     * that it should stop executing the callback.
     *
     * @throws Exception an exception while processing the item
     */
    void call(T item) throws Exception;
}
