package org.narses.narsion.dev.math;

import org.narses.narsion.util.Pair;

public class MathUtil {

    public static <E> Pair<E, E>[] makePairs(E[] values) {

        Pair[] pairs = new Pair[values.length];

        for (int i = 0; i < values.length; i++) {
            E valueA = values[i];
            E valueB;
            if (i == values.length - 1)
                valueB = values[0];
            else
                valueB = values[i + 1];

            pairs[i] = new Pair<>(valueA, valueB);
        }

        return pairs;
    }
}
