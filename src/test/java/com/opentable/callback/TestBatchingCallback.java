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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.junit.Test;


public class TestBatchingCallback
{
    @Test
    public void testBatchingCallback() throws Exception
    {
        CallbackCollector<List<String>> collector = new CallbackCollector<>();
        try (BatchingCallback<String> batcher = BatchingCallback.batchInto(2, collector)) {
            batcher.call("a");
            batcher.call("b");
            batcher.call("c");
            batcher.commit();
            batcher.call("d");
            batcher.call("e");
            batcher.call("f");
        }

        assertEquals(of(
                of("a", "b"),
                of("c"),
                of("d", "e"),
                of("f")
            ), collector.getItems());
    }

    private List<List<String>> of(List<String>... lists) {
        List<List<String>> listy = new ArrayList<>();
        for (List<String> list : lists) {
            listy.add(list);
        }
        return listy;
    }

    public static List<String> of(String... strings) {
        List<String> s = new ArrayList<>();
        for (String ss : strings) {
            s.add(ss);
        }
        return s;
    }

    @Test
    public void testCallbackRefused() throws Exception
    {
        final List<Collection<String>> items = new ArrayList<>();
        Callback<Collection<String>> callback = new Callback<Collection<String>>() {
            @Override
            public void call(Collection<String> item) throws Exception
            {
                if (items.size() > 1) {
                    throw new CallbackRefusedException();
                }
                items.add(item);
            }
        };

        try (BatchingCallback<String> batcher = BatchingCallback.<String>batchInto(2, callback)) {
            Callbacks.stream(batcher, "a", "b", "c", "d", "e", "f", "g");
        }

        assertEquals(of(
                of("a", "b"),
                of("c", "d")
            ), items);
    }
}
