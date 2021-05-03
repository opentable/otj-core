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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.MoreExecutors;

import org.junit.Test;

public class TestExecutorBatchingCallback {
    private ExecutorService directExecutor = MoreExecutors.newDirectExecutorService();

    @Test
    public void testGood() throws Exception
    {
        CallbackCollector<List<String>> out = new CallbackCollector<>();
        BatchingCallback<String> batcher = BatchingCallback.batchInto(2, directExecutor, out, false);

        batcher.call("a");
        batcher.call("b");
        batcher.call("c");
        batcher.call("d");
        batcher.call("e");

        batcher.commit();

        assertEquals(of(of("a", "b"), of("c", "d"), of("e")), out.getItems());
    }

    private List<List<String>> of(List<String>... lists) {
        List<List<String>> listy = new ArrayList<>();
        for (List<String> list : lists) {
            listy.add(list);
        }
        return listy;
    }

    List<String> of(String... strings) {
        List<String> s = new ArrayList<>();
        for (String ss : strings) {
            s.add(ss);
        }
        return s;
    }

    @Test
    public void testFailSlow() throws Exception
    {
        final Exception e1 = new Exception();
        final Exception e2 = new Exception();

        Callback<List<String>> out = new Callback<List<String>>() {
            @Override
            public void call(List<String> item) throws Exception
            {
                switch (item.get(0)) {
                case "a":
                    throw e1;
                case "c":
                    return;
                case "e":
                    throw e2;
                }
            }
        };

        BatchingCallback<String> batcher = BatchingCallback.batchInto(2, directExecutor, out, false);

        batcher.call("a");
        batcher.call("b");
        batcher.call("c");
        batcher.call("d");
        batcher.call("e");

        try {
            batcher.commit();
            fail();
        } catch (BatchingCallbackExecutionException bcee) {
            assertEquals(2, bcee.getSuppressed().length);
            assertSame(e1, bcee.getSuppressed()[0]);
            assertSame(e2, bcee.getSuppressed()[1]);
        }
    }

    @Test
    public void testFailFast() throws Exception
    {
        final Exception e1 = new Exception();
        final Exception e2 = new Exception();

        Callback<List<String>> out = new Callback<List<String>>() {
            @Override
            public void call(List<String> item) throws Exception
            {
                switch (item.get(0)) {
                case "a":
                    throw e1;
                case "c":
                    return;
                case "e":
                    throw e2;
                }
            }
        };

        BatchingCallback<String> batcher = BatchingCallback.batchInto(2, directExecutor, out, true);

        batcher.call("a");
        batcher.call("b");
        try {
            batcher.call("c");
            fail();
        } catch (BatchingCallbackExecutionException bcee) {
            assertEquals(1, bcee.getSuppressed().length);
            assertSame(e1, bcee.getSuppressed()[0]);
        }
        try {
            batcher.call("d");
            batcher.call("e");
            batcher.call("f");
            fail();
        } catch (CallbackRefusedException e) {
        }
    }

    @Test(expected=BatchingCallbackExecutionException.class)
    public void testSingleExceptionFails() throws Exception
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Callback<List<String>> out = new Callback<List<String>>() {
                @Override
                public void call(List<String> item) throws Exception
                {
                    Thread.sleep(10);
                    throw new IllegalStateException("boom!");
                }
            };
            try (BatchingCallback<String> batcher = BatchingCallback.batchInto(2, executor, out, true)) {
                batcher.call("a");
            }
        } finally {
            executor.shutdownNow();
        }
    }
}
