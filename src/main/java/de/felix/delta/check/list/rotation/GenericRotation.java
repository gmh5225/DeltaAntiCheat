/**
 * As the check name says, there are just generic rotation checks, every anti cheat needs
 */

package de.felix.delta.check.list.rotation;

import de.felix.delta.check.Check;
import de.felix.delta.check.CheckInfo;
import de.felix.delta.check.CheckType;
import de.felix.delta.check.alert.AlertTagBuilder;
import de.felix.delta.data.DataHolder;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;

@CheckInfo(checkName = "GenericRotation", checkType = CheckType.ROTATION, punishable = false)
public class GenericRotation extends Check {
    public GenericRotation(DataHolder dataHolder) {
        super(dataHolder);
    }
    @Override
    public void handle(PlayerEvent packet) {
        if (packet instanceof PlayerMoveEvent) {
            if (Math.abs(getDataHolder().rotationData.getRotation().pitch) > 90)
                getAlertBuilder().setAlertMessage("Pitch greater or lower than 90 || -90").runAlert(new AlertTagBuilder());

            double yaw = getDataHolder().rotationData.getRotation().yaw;
            yaw = ((yaw + 180) % 360) - 180;

            if (yaw > -180 && yaw < 180 && Math.abs(getDataHolder().rotationData.getRotation().deltaYaw) > 320 && Math.abs(getDataHolder().rotationData.getLastDeltaYaw()) < 30)
                getAlertBuilder().setAlertMessage("Unlike yaw acceleration").runAlert(new AlertTagBuilder());
        }
    }
}
