package de.felix.delta.util;

public class Rotation {

    public float yaw, pitch, deltaYaw, deltaPitch;

    public Rotation(final float yaw, final float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Rotation(Rotation rotation) {
        this.yaw = rotation.yaw;
        this.pitch = rotation.pitch;
        this.deltaYaw = rotation.deltaYaw;
        this.deltaPitch = rotation.deltaPitch;
    }

    public Rotation(final float yaw, final float pitch, final float deltaYaw, final float deltaPitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.deltaYaw = deltaYaw;
        this.deltaPitch = deltaPitch;
    }


    public Rotation delta(final Rotation rotation) {
        return new Rotation(yaw - rotation.yaw, pitch - rotation.pitch);
    }

    public void normalize() {
        yaw = clamp(yaw, -180, 180);
        pitch = clamp(pitch, -90, 90);
    }
    public float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public Rotation lerp(Rotation a, Rotation b, float t) {
        float newYaw = a.yaw + (b.yaw - a.yaw) * t;
        float newPitch = a.pitch + (b.pitch - a.pitch) * t;
        float newDeltaYaw = a.deltaYaw + (b.deltaYaw - a.deltaYaw) * t;
        float newDeltaPitch = a.deltaPitch + (b.deltaPitch - a.deltaPitch) * t;
        return new Rotation(newYaw, newPitch, newDeltaYaw, newDeltaPitch);
    }
}
