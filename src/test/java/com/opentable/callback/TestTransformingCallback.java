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

import static com.opentable.callback.TransformedCallback.transform;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;

public class TestTransformingCallback
{
    @Test
    public void testSimple() throws Exception
    {
        CallbackCollector<String> callback = new CallbackCollector<>();
        Callback<Integer> tested = transform(callback, new Function<Integer, String>() {
            @Override
            public String apply(Integer input)
            {
                return input == null ? null : Integer.toHexString(input);
            }
        });

        tested.call(null);
        tested.call(15);
        tested.call(0xDEADBEEF);

        List<String> expected = new ArrayList<>();
        expected.add(null);
        expected.add("f");
        expected.add("deadbeef");
        assertEquals(expected, callback.getItems());
    }

    @Test
    public void testWildcards() throws Exception
    {
        CallbackCollector<Object> callback = new CallbackCollector<>();
        Callback<String> strCallback = transform(callback, new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s.toString();
            }
        });

        strCallback.call("foo");
        strCallback.call("bar");

        Function<Super, Class<?>> superFunction = new Function<Super, Class<?>>() {
            @Override
            public Class<?> apply(Super input)
            {
                return input.getClass();
            }
        };

        Callback<Super> superCallback = transform(callback, superFunction);
        Callback<Sub> subCallback = transform(callback, superFunction);

        superCallback.call(new Super());
        superCallback.call(new Sub());
        subCallback.call(new Sub());

        List<Object> weird = new ArrayList<>();
        weird.add("foo");
        weird.add("bar");
        weird.add(Super.class);
        weird.add(Sub.class);
        weird.add(Sub.class);
        assertEquals(weird, callback.getItems());
    }

    static class Super { }
    static class Sub extends Super { }
}
