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

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.function.BiFunction;

public class OptionalsTest {
    @Test
    public void unless_returnLeftIfPresent() {
        Optional<Integer> left = Optional.of(3);
        Optional<Integer> result = Optionals.unless(left, () -> 7);
        assertEquals(Optional.of(3), result);
    }

    @Test
    public void unless_returnRightIfLeftAbsent() {
        Optional<Integer> left = Optional.empty();
        Optional<Integer> result = Optionals.unless(left, () -> 7);
        assertEquals(Optional.of(7), result);
    }

    @Test
    public void unless_doesntEvaluateRightSideIfLeftIsPresent() {
        Optional<Integer> left = Optional.of(3);
        final boolean[] hit = {false};
        Optional<Integer> result = Optionals.unless(left, () -> {
            hit[0] = true;
            return 7;
        });
        assertEquals(Integer.valueOf(3), result.get());
        assertFalse(hit[0]);
    }

    @Test
    public void unlessFlat_returnLeftIfPresent() {
        Optional<Integer> left = Optional.of(3);
        Optional<Integer> result = Optionals.unlessOpt(left, () -> Optional.of(7));
        assertEquals(Optional.of(3), result);
    }

    @Test
    public void unlessFlat_returnRightIfLeftAbsent() {
        Optional<Integer> left = Optional.empty();
        Optional<Integer> result = Optionals.unlessOpt(left, () -> Optional.of(7));
        assertEquals(Optional.of(7), result);
    }

    @Test
    public void unlessFlat_doesntEvaluateRightSideIfLeftIsPresent() {
        Optional<Integer> left = Optional.of(3);
        final boolean[] hit = {false};
        Optional<Integer> result = Optionals.unlessOpt(left, () -> {
            hit[0] = true;
            return Optional.of(7);
        });
        assertEquals(Integer.valueOf(3), result.get());
        assertFalse(hit[0]);
    }

    @Test
    public void map2_severalScenarios() {
        BiFunction<Integer, String, String> prependIntegerToString = (i, s) -> i + s;

        assertEquals(Optional.empty(),
                Optionals.map2(Optional.empty(), Optional.empty(), prependIntegerToString));
        assertEquals(Optional.empty(),
                Optionals.map2(Optional.empty(), Optional.of("foo"), prependIntegerToString));
        assertEquals(Optional.empty(),
                Optionals.map2(Optional.of(3), Optional.empty(), prependIntegerToString));
        assertEquals(Optional.of("3foo"),
                Optionals.map2(Optional.of(3), Optional.of("foo"), prependIntegerToString));
    }
}
