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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utilities for producing Streams from inconvenient data structures.
 */
public final class Streams {

    /**
     * Make Enumerations less painful to deal with by wrapping them in a Stream.
     * <p>
     * Unfortunately getters from classes such as HttpServletRequests produce Enumerations, which
     * can't be dealt used without old-school Iterator style next/hasNext iteration and the dirty
     * mutation complexity which goes along with it.
     * <p>
     * This method also avoids the creation of an intermediate List or other data structure.
     *
     * @param enumeration - the enumeration to wrap
     * @param <T>         - the things in the enuneration
     * @return a nice stream items in the enumeration
     */
    public static <T> Stream<T> of(Enumeration<T> enumeration) {
        final Iterator<T> iterator = new Iterator<T>() {
            public T next() {
                return enumeration.nextElement();
            }

            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }
        };
        final Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED);
        return StreamSupport.stream(spliterator, false);
    }
}
