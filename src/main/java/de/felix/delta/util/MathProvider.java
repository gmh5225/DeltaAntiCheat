package de.felix.delta.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MathProvider {

    public static double angle(Point a, Point b) {
        final double dot = Math.min(Math.max(a.dot(b) / (a.length() * b.length()), -1), 1);
        return Math.acos(dot);
    }

}
