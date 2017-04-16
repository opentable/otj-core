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

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

import java.util.Enumeration;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

public class StreamsTest {
    @Test
    public void enumerationConversion() {
        final Enumeration<String> enumeration = createEnumeration();

        final Stream<String> stream = Streams.of(enumeration);

        final List<String> list = stream.map(s -> s + ":" + s.length())
                .collect(toList());
        assertEquals(3, list.size());
        assertEquals("aaa:3", list.get(0));
        assertEquals("bb:2", list.get(1));
        assertEquals("c:1", list.get(2));
    }

    private Enumeration<String> createEnumeration() {
        return new Enumeration<String>() {
            int index = 0;
            String[] items = new String[] { "aaa", "bb", "c"};
            @Override
            public boolean hasMoreElements() {
                return index < items.length;
            }

            @Override
            public String nextElement() {
                return items[index++];
            }
        };
    }
}
