package de.felix.delta.check.alert;

import java.util.StringJoiner;

public class TagBuilder {
    private final StringJoiner stringJoiner;

    public TagBuilder() {
        this.stringJoiner = new StringJoiner(", ");
    }

    public TagBuilder add(String tag) {
        this.stringJoiner.add(tag);
        return this;
    }

    @Override
    public String toString() {
        return this.stringJoiner.toString();
    }
}