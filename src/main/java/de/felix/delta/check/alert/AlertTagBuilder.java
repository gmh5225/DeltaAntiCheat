package de.felix.delta.check.alert;

import java.util.Objects;
import java.util.StringJoiner;

public class AlertTagBuilder {
    private final StringJoiner stringJoiner;

    public AlertTagBuilder() {
        this.stringJoiner = new StringJoiner(", ");
    }

    public AlertTagBuilder add(String tag) {
        this.stringJoiner.add(tag);
        return this;
    }

    public AlertTagBuilder add(Objects tag) {
        this.stringJoiner.add(tag.toString());
        return this;
    }

    @Override
    public String toString() {
        return this.stringJoiner.toString();
    }
}