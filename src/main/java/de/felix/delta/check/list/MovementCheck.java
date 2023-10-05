package de.felix.delta.check.list;

import de.felix.delta.check.Check;
import de.felix.delta.check.CheckInfo;
import de.felix.delta.check.CheckType;
import de.felix.delta.data.DataHolder;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;

@CheckInfo(checkName = "Movement", checkType = CheckType.MOVEMENT, punishable = false)
public class MovementCheck extends Check {

    private long tickTime;

    public MovementCheck(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public void handle(PlayerEvent packet) {
        if (packet instanceof PlayerMoveEvent) {
            System.out.println(getDataHolder().worldData.serverGround);
        }
    }
}
