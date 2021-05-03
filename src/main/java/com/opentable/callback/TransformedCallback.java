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


import java.util.function.Function;

/**
 * A callback that can process an item of type A,
 * by first using a provided transformer to transform
 * it from A to B and then executing a given callback
 * processes type B
 *
 * @param <A> the type of item to process
 * @param <B> the type of item processed by the provided callback
 */
public class TransformedCallback<A, B> implements Callback<A>
{
    private final Callback<? super B> callback;
    private final Function<? super A, ? extends B> transformer;

    /**
     * Create a new Callback which transforms its items according to a {@link Function}
     * and then invokes the original callback.
     * @param callback the callback that processes items of type B
     * @param transformer the transformer that converts the item from type A to type B
     * @param <A> the type of item that will be provided as input
     * @param <B> the type of item processed by the provided callback
     */
    public static <A, B> Callback<A> transform(Callback<? super B> callback, Function<? super A, ? extends B> transformer)
    {
        return new TransformedCallback<A, B>(callback, transformer);
    }

    /**
     * Create a new Callback which transforms its items according to a {@link Function}
     * and then invokes the original callback.
     * @param callback the callback that processes items of type B
     * @param transformer the transformer that converts the item from type A to type B
     */
    TransformedCallback(Callback<? super B> callback, Function<? super A, ? extends B> transformer)
    {
        this.callback = callback;
        this.transformer = transformer;
    }

    @Override
    public void call(A item) throws Exception
    {
        callback.call(transformer.apply(item));
    }
}
