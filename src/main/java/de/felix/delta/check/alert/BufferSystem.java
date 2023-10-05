package de.felix.delta.check.alert;

import lombok.Getter;

public class BufferSystem {

    @Getter
    private int buffer;

    private final int limit;

    public BufferSystem(final int limit) {
        this.limit = limit;
    }

    public void increment(int size) {
        this.buffer += size;
    }

    public void reward() {
        buffer = (int) Math.max(buffer - 0.5, 0);
    }

    public boolean isBuffered() {
        return buffer > limit;
    }
}
