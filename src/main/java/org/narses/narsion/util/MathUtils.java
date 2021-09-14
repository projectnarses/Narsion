package org.narses.narsion.util;

public class MathUtils {
    public static double lerp(double min, double max, double percentage) {
        return min + percentage * (max - min);
    }

    public static int lerp(int min, int max, double percentage) {
        return (int) (min + percentage * (max - min));
    }
}
