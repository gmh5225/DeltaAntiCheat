package de.felix.delta.data;

import cc.funkemunky.api.reflections.impl.MinecraftReflection;
import cc.funkemunky.api.tinyprotocol.api.ProtocolVersion;
import cc.funkemunky.api.utils.BoundingBox;
import cc.funkemunky.api.utils.world.types.SimpleCollisionBox;
import de.felix.delta.check.Check;
import de.felix.delta.data.datas.*;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class DataHolder {
    public final Set<Check> addedChecks = new HashSet<>();
    public MovementData movementData;
    public TeleportData teleportData;
    public EnemyData enemyData;
    public WorldData worldData;
    public RotationData rotationData;

    public UUID uuid;
    public Player player;

    public SimpleCollisionBox box = new SimpleCollisionBox();

    public ProtocolVersion playerVersion = ProtocolVersion.UNKNOWN;

    private Object playerConnection;

    public DataHolder(Player holder) {
        super();
        this.player = holder;
        this.uuid = player.getUniqueId();
        this.teleportData = new TeleportData(uuid);
        this.movementData = new MovementData(uuid);
        this.worldData = new WorldData(uuid);
        this.enemyData = new EnemyData(uuid);
        this.rotationData = new RotationData(uuid);
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
