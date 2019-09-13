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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

/**
 * Utility functions to help alternate values stay in optionals when evaluating the absent case.
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "WeakerAccess"})
public final class Optionals {

    private Optionals() {
        /* utility class */
    }

    /**
     * Provide approximation of scala's Option.orElse(=&gt;Y). For when you want to evaluate
     * alternate values when an optional value is absent, where the alternates are not Optionals,
     * and the result will also be an Optional. (What happens in an Optional stays in an Optional.)
     * If first is absent, then lazily evaluate second, resulting in another Option.
     * <p>
     * It's important to note that the lambda in apply will only get evaluated if the argument "first"
     * is not present.
     * </p>
     * e.g.: z = unless(x).apply(() -&gt; somePossiblyExpensiveExpressionResultingInAnX);
     *
     * @param first the value to check for presence and use if available
     * @param second alternate value supplier, only evaluate if first is absent
     * @param <X> type of value specified contained in first's Optional.
     *
     * @return first if first is present, otherwise the supplier is evaluated (see example above)
     */
    public static <X> Optional<X> unless(@Nonnull Optional<X> first, Supplier<X> second) {
        return first.isPresent() ? first : Optional.of(second.get());
    }

    /**
     * Provide approximation of scala's Option.orElse(=&gt;Y). For when you want to evaluate
     * alternate values when an optional value is absent, where the alternates ARE also Optionals,
     * and the result will also be an Optional. (What happens in an Optional stays in an Optional.)
     * If first is absent, then lazily evaluate second, resulting in another Option.
     * <p>
     *     This differs to the above in that the alternate values are optional too.
     * </p>
     * <p>
     * It's important to note that the second parameter lambda will only get evaluated if the argument
     * "first" is empty.
     * </p>
     * e.g.: z = unlessOpt(x).apply(() -&gt; somePossiblyExpensiveExpressionResultingInAnOptionalX);
     *
     * @param first the value to check for presence and use if available
     * @param second a supplier which will be evaluated if "first" is absent
     * @param <X> type of value specified contained in first's Optional.
     * @return if first is present, otherwise the supplier is evaluated (see example above).
     */
    public static <X> Optional<X> unlessOpt(@Nonnull Optional<X> first, Supplier<Optional<X>> second) {
        return first.isPresent() ? first : second.get();
    }

    /**
     * Combines two optionals to get a third one based on the values of the given ones if they are present.
     * @param oa First optional.
     * @param ob Second optional.
     * @param combiner Function that combines the values of the given optionals.
     * @param <X> type of first optional.
     * @param <Y> type of the second optional.
     * @param <Z> return type of the combiner function.
     * @return The combined result wrapped into an optional if values of both optionals are present.
     *         Optional.empty() otherwise.
     */
    public static <X, Y, Z> Optional<Z> combineWith(Optional<X> oa, Optional<Y> ob, BiFunction<X, Y, Z> combiner) {
        return oa.flatMap(x -> ob.map(y -> combiner.apply(x, y)));
    }

    /**
     * When wrapped around a Map, allows get() operations to produce an Optional rather than a Nullable
     * @param <K> type of map keys
     * @param <V> type of map values
     */
    @FunctionalInterface
    public interface MapAdapter<K,V> {
        /**
         * Get item from map
         * @param key the key to lookup
         * @return an optional holding the value from the map, will be an empty optional if the map does not contain the key, or if it does but the value is null
         */
        Optional<V> getOpt(K key);
    }

    /**
     * Produces a MapAdapter for the given Map.
     * @param <K> type of map keys
     * @param <V> type of map values
     */
    public static <K,V> MapAdapter<K,V> mapAdapter(final Map<K,V> map) {
        return key -> Optional.ofNullable(map.get(key));
    }

    /**
     * Adapter to create a stream out of an Optional. Because Java streams can't flatMap functions which
     * return Optional. Lame.
     * <p>
     *     If there's something in there, make a stream out of it. If not, make an empty stream.
     *
     * @param source the Optional to convert to a Stream
     * @param <T> the type of objects to be held in the resulting stream
     * @return the stream of T's, which will be empty if the source is empty
     */
    public static <T> Stream<T> stream(Optional<T> source) {
        return source.map(Stream::of).orElseGet(Stream::empty);
    }

    /**
     * Adapter to create an Optional out of the first element of a List. If the list is empty, we get
     * an empty optional. Also, if the first element is null, return an empty optional.
     *
     * @param <T> the type of objects in the list
     * @param list the list to look at
     * @return the possible first element in list
     */
    public static <T> Optional<T> firstInList(List<T> list) {
        return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.get(0));
    }

    /**
     * Get an item from a map, wrapping it in an Optional
     * @param map the map on which to perform the lookup
     * @param key the key to look up
     * @param <K> the type of key
     * @param <V> the type of values in the map
     * @return Optional-wrapped V
     */
    public static <K,V> Optional<V> getOpt(Map<K,V> map, K key) {
        return Optional.ofNullable(map.get(key));
    }
}

