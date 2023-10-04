package de.felix.delta.util;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MovementStorage {

    @Getter
    @Setter
    private Vector currentPosition, lastPosition;

    private final List<PointWithTime> pointHistory = new ArrayList<>();

    public void addMovementPoint(Vector point) {
        pointHistory.add(new PointWithTime(point, System.currentTimeMillis()));
        currentPosition = point;
    }

    public Vector getPointBehindTick(final int ticksBehind, boolean threshold) {
        final long targetTime = threshold ? System.currentTimeMillis() - ticksBehind * 50L : System.currentTimeMillis() - ticksBehind;
        PointWithTime pointWithTime = null;
        for (PointWithTime pwt : pointHistory) {
            if (pwt.getTime() <= targetTime) {
                pointWithTime = pwt;
                break;
            }
        }
        return pointWithTime != null ? pointWithTime.getPoint() : null;
    }

    @Getter
    private static class PointWithTime {
        private final Vector point;
        private final long time;

        public PointWithTime(Vector point, long time) {
            this.point = point;
            this.time = time;
        }
    }
}