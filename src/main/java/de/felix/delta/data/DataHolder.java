package de.felix.delta.data;

import cc.funkemunky.api.reflections.impl.MinecraftReflection;
import de.felix.delta.check.Check;
import de.felix.delta.data.datas.MovementData;
import de.felix.delta.data.datas.TeleportData;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DataHolder {
    public final Set<Check> addedChecks = new HashSet<>();
    public MovementData movementData;

    public TeleportData teleportData;

    public UUID uuid;
    public Player player;

    private Object playerConnection;

    public DataHolder(Player holder) {
        super();
        this.player = holder;
        this.uuid = player.getUniqueId();
        this.teleportData = new TeleportData(uuid);
        this.movementData = new MovementData(uuid);
    }

    public Object getPlayerConnection() {
        if(playerConnection == null)
            this.playerConnection = MinecraftReflection.getPlayerConnection(player);
        return this.playerConnection;
    }

    public boolean addCheck(Check check) {
        if (addedChecks.contains(check)) {
            return false;
        }
        addedChecks.add(check);
        return true;
    }

}
