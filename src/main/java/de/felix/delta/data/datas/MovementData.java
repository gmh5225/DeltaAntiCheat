package de.felix.delta.data.datas;

import de.felix.delta.data.RegistrableDataHolder;
import de.felix.delta.util.MovementStorage;
import lombok.Getter;
import org.bukkit.util.Vector;

import java.util.UUID;

@Getter
public class MovementData extends RegistrableDataHolder {

    public MovementStorage movementStorage = new MovementStorage();

    public float deltaXZ, deltaY, deltaXYZ;

    public MovementData(UUID holder) {
        super(holder);
    }

    public void process(final Vector position) {
        movementStorage.setLastPosition(movementStorage.getCurrentPosition());
        movementStorage.addMovementPoint(position);
        this.deltaXYZ = (float) movementStorage.getCurrentPosition().distance(movementStorage.getLastPosition());
        this.deltaXZ = (float) Math.sqrt(Math.pow(movementStorage.getCurrentPosition().getX() - movementStorage.getLastPosition().getX(), 2) + Math.pow(movementStorage.getCurrentPosition().getZ() - movementStorage.getLastPosition().getZ(), 2));
    }
}
