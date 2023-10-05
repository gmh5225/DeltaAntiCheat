package de.felix.delta.check.list.rotation.mousemovement;


import de.felix.delta.util.Point;

import java.util.List;

public class MouseMovementStats {
    public static class Stats {
        public double maxSpeed;
        public double minSpeed;
        public double maxAngle;

        public Stats(double maxSpeed, double minSpeed, double maxAngle) {
            this.maxSpeed = maxSpeed;
            this.minSpeed = minSpeed;
            this.maxAngle = maxAngle;
        }
    }

    //Improved version of a check from hawk anti-cheat
    public static Stats calculateMouseMovementStats(List<Point> points) {
        double maxSpeed = 0;
        double minSpeed = Double.POSITIVE_INFINITY;
        double maxAngle = 0;

        for (int i = 1; i < points.size(); i++) {
            final Point lastMousePoint = points.get(i - 1);
            final Point currentMousePoint = points.get(i);
            final double speed = Math.hypot(currentMousePoint.getX() - lastMousePoint.getX(), currentMousePoint.getY() - lastMousePoint.getY());
            final double lastSpeed = i > 1 ? Math.hypot(points.get(i - 2).getX() - lastMousePoint.getX(), points.get(i - 2).getY() - lastMousePoint.getY()) : 0;
            double angle = lastSpeed != 0 ? -Math.toDegrees(Math.atan2(currentMousePoint.getY() - lastMousePoint.getY(), currentMousePoint.getX() - lastMousePoint.getX())) : 0;

            if (Double.isNaN(angle))
                angle = 0;

            maxSpeed = Math.max(speed, maxSpeed);
            minSpeed = Math.min(speed, minSpeed);
            maxAngle = Math.max(angle, maxAngle);
        }

        return new Stats(maxSpeed, minSpeed, maxAngle);
    }
}
