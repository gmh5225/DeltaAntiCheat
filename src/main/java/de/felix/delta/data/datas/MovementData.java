package de.felix.delta.data.datas;

import de.felix.delta.data.RegistrableDataHolder;
import de.felix.delta.util.MovementStorage;
import org.bukkit.util.Vector;

import java.util.UUID;

public class MovementData extends RegistrableDataHolder {

    public MovementStorage movementStorage = new MovementStorage();

    public MovementData(UUID holder) {
        super(holder);
    }

    public void process(final Vector position) {
        movementStorage.setLastPosition(movementStorage.getCurrentPosition());
        movementStorage.addMovementPoint(position);
    }
}
