package de.felix.delta.data;

import java.util.UUID;

public abstract class RegistrableDataHolder {
    public UUID holder;
    public RegistrableDataHolder(UUID holder) {
        this.holder = holder;
    }
}
