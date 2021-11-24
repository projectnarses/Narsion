package org.narses.narsion.util;

import java.util.function.Function;

public class StringUtils {
    public static <E> E getClosestNamed(
            String name,
            Iterable<E> values,
            double minimumSimilarity,
            Function<E, String> getName
    ) {
        E closest = null;
        double closestDistance = 0;
        for (E value : values) {
            double distance = net.minestom.server.utils.StringUtils.jaroWinklerScore(getName.apply(value), name);
            if (distance > closestDistance) {
                closest = value;
                closestDistance = distance;
            }
        }
        if (closestDistance < minimumSimilarity) {
            return null;
        }
        return closest;
    }
}
