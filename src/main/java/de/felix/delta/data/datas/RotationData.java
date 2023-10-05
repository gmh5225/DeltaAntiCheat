package de.felix.delta.data.datas;

import cc.funkemunky.api.tinyprotocol.packet.out.WrappedOutEntityHeadRotation;
import de.felix.delta.data.RegistrableDataHolder;
import de.felix.delta.util.Rotation;
import lombok.Getter;

import java.util.UUID;

public class RotationData extends RegistrableDataHolder {

    @Getter
    private Rotation rotation;

    private float yaw, pitch, lYaw, lPitch, deltaYaw, deltaPitch;


    public RotationData(UUID holder) {
        super(holder);
    }

    public void process(final WrappedOutEntityHeadRotation wrappedOutEntityHeadRotation) {
        this.lYaw = this.yaw;
        this.lPitch = this.pitch;
        this.yaw = wrappedOutEntityHeadRotation.getPlayer().getLocation().getYaw();
        this.pitch = wrappedOutEntityHeadRotation.getPlayer().getLocation().getPitch();
        this.deltaYaw = yaw - lYaw;
        this.deltaPitch = pitch - lPitch;
        this.rotation = new Rotation(yaw, pitch, deltaYaw, deltaPitch);
    }
}
