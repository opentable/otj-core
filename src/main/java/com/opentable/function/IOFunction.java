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

import java.io.IOException;

/**
 * A function that takes in input and produces output, potentially throwing an IO exception
 *
 * @param <In> the input type
 * @param <Out> the output type
 */
@FunctionalInterface
public interface IOFunction<In, Out>
{
    /**
     * Apply the function to take in input and produce output
     * @param in the input
     * @return the output
     * @throws IOException a I/O exception processing the input/output
     */
    Out apply(In in) throws IOException;
}
