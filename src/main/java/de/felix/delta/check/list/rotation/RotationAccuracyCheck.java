/*
This check checks the accuracy of the player's rotation compared to the calculated rotations to the enemy.
 */

package de.felix.delta.check.list.rotation;

import cc.funkemunky.api.utils.MathHelper;
import de.felix.delta.DeltaPlugin;
import de.felix.delta.check.Check;
import de.felix.delta.check.CheckInfo;
import de.felix.delta.check.CheckType;
import de.felix.delta.check.alert.AlertTagBuilder;
import de.felix.delta.data.DataHolder;
import de.felix.delta.util.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@CheckInfo(checkName = "RotationAccuracy", checkType = CheckType.ROTATION, punishable = false)
public class RotationAccuracyCheck extends Check {

    private final HashMap<UUID, Integer> playerPerfectRotations = new HashMap<>();

    private final HashMap<UUID, Long> playerTimestamps = new HashMap<>();

    private final double ACCURACY_THRESHOLD;

    private final int MAX_COUNT;

    public ArrayList<Float> differences = new ArrayList<>();

    public RotationAccuracyCheck(DataHolder dataHolder) {
        super(dataHolder);
        ACCURACY_THRESHOLD = DeltaPlugin.getInstance().checkManager.getValueDouble(CheckType.ROTATION, "accuracy-threshold");
        MAX_COUNT = DeltaPlugin.getInstance().checkManager.getValueInt(CheckType.ROTATION, "max-count");
    }

    @Override
    public void handle(PlayerEvent packet) {
        if (packet instanceof PlayerMoveEvent) {
            if (getDataHolder().enemyData.enemy == null)
                differences.clear();

            if (getDataHolder().enemyData.enemy == null) return;

            final Rotation targetRotation = getTargetRotations(getDataHolder().enemyData.enemy, getDataHolder());

            final float perfectRotation = MathHelper.wrapAngleTo180_float(targetRotation.yaw);

            final float yaw = MathHelper.wrapAngleTo180_float(getDataHolder().rotationData.getRotation().yaw);

            checkPerfectHit(getDataHolder(), perfectRotation, yaw);
        }
    }

    private void checkPerfectHit(DataHolder dataHolder, float perfectRotation, float yaw) {
        final UUID playerUUID = dataHolder.getPlayer().getUniqueId();
        final float difference = Math.abs(yaw - perfectRotation);

        if (difference <= ACCURACY_THRESHOLD) {
            playerTimestamps.put(playerUUID, System.currentTimeMillis());
            playerPerfectRotations.put(playerUUID, playerPerfectRotations.getOrDefault(playerUUID, 0) + 1);
        } else {
            Long lastPerfectHit = playerTimestamps.get(playerUUID);
            if (lastPerfectHit != null && System.currentTimeMillis() - lastPerfectHit < 10000) {
                int count = playerPerfectRotations.getOrDefault(playerUUID, 0);
                if (count > MAX_COUNT) {
                    final AlertTagBuilder tagBuilder = new AlertTagBuilder();
                    tagBuilder.add("Count " + count);
                    tagBuilder.add("Difference " + difference);
                    tagBuilder.add("Yaw " + yaw);
                    tagBuilder.add("Perfect " + perfectRotation);
                    tagBuilder.add("Delta " + Math.abs(yaw - perfectRotation));
                    getAlertBuilder().setAlertMessage("Too high accuracy").runAlert(tagBuilder);
                    playerPerfectRotations.put(dataHolder.getPlayer().getUniqueId(), 0);
                }
            }
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
