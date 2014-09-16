package com.opentable.util;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
}
