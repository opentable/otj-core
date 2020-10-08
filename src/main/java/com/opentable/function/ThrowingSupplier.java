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
package com.opentable.function;

import java.util.function.Supplier;

/**
 * An alternative to {@link Supplier} that throws a checked {@link Exception}
 *
 * @param <T> the type of the result of the supplier.
 */
@FunctionalInterface
public interface ThrowingSupplier<T> {
    /**
     * Gets a result.
     * @return the result.
     * @throws Exception the possible error when trying to get the result.
     */
    T get() throws Exception;
}
