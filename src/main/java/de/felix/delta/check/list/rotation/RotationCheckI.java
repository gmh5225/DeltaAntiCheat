/*
This check checks the accuracy of the player's rotation compared to the calculated rotations to the enemy.
 */

package de.felix.delta.check.list.rotation;

import cc.funkemunky.api.utils.MathHelper;
import de.felix.delta.check.Check;
import de.felix.delta.check.CheckInfo;
import de.felix.delta.check.CheckType;
import de.felix.delta.check.alert.AlertTagBuilder;
import de.felix.delta.check.alert.BufferSystem;
import de.felix.delta.data.DataHolder;
import de.felix.delta.util.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;

@CheckInfo(checkName = "Rotation", checkType = CheckType.ROTATION, punishable = false)
public class RotationCheckI extends Check {

    public ArrayList<Float> differences = new ArrayList<>();

    private final BufferSystem bufferSystem = new BufferSystem(100);

    public RotationCheckI(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public void handle(PlayerEvent packet) {
        if (packet instanceof PlayerMoveEvent) {

            if (getDataHolder().enemyData.enemy == null)
                differences.clear();


            final AlertTagBuilder tagBuilder = new AlertTagBuilder();

            if (getDataHolder().enemyData.enemy == null) return;

            final Rotation targetRotation = getTargetRotations(getDataHolder().enemyData.enemy, getDataHolder());

            final float perfectRotation = MathHelper.wrapAngleTo180_float(targetRotation.yaw);

            final float yaw = MathHelper.wrapAngleTo180_float(getDataHolder().rotationData.getRotation().yaw);

            final float difference = Math.abs(yaw - perfectRotation) * (getDataHolder().movementData.deltaXZ);

            differences.add(difference);

            final float average = (float) differences.stream().mapToDouble(Float::doubleValue).average().orElse(0.0);

            tagBuilder.add("Avg: " + average);

            tagBuilder.add("Yaw: " + String.valueOf(yaw));

            if (average < 2 && differences.size() > 20)
                bufferSystem.increment(1);
            else
                bufferSystem.reward();

            if (bufferSystem.isBuffered()) {
                getAlertBuilder().setAlertMessage("Too high accuracy").runAlert(tagBuilder);
                differences.clear();
            }
            System.out.println(average + " " + bufferSystem.getBuffer());
        }
    }

    // This method calculates the rotations to the enemy and returns them.
    // EntityLookHelper.java - Mod Coder Pack 1.8.9 onUpdateLook() line 52
    private Rotation getTargetRotations(final Entity target, final DataHolder dataHolder) {
        final Vector playerPos = new Vector(dataHolder.getPlayer().getLocation().getX(), dataHolder.getPlayer().getLocation().getY(), dataHolder.getPlayer().getLocation().getZ());
        final Vector entityPos = new Vector(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getZ());
        final double deltaX = entityPos.getX() - playerPos.getX();
        final double deltaY = entityPos.getY() + (dataHolder.getPlayer().getEyeHeight() - playerPos.getY());
        final double deltaZ = entityPos.getZ() - playerPos.getZ();
        final double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        final float yaw = (float) (Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0);
        final float pitch = (float) -Math.toDegrees(Math.atan2(deltaY, horizontalDistance));
        return new Rotation(yaw, pitch);
    }
}
