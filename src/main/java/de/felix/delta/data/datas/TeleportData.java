package de.felix.delta.data.datas;

import de.felix.delta.data.RegistrableDataHolder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.UUID;

@Getter
@Setter
public class TeleportData extends RegistrableDataHolder {

    public Location lastTeleport;
    public long lastTeleportTime;


    public TeleportData(UUID holder) {
        super(holder);
    }
}
