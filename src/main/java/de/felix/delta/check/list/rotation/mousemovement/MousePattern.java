/*
 * Highly inspired from hawk Anti-Cheat
 */

package de.felix.delta.check.list.rotation.mousemovement;

import de.felix.delta.check.Check;
import de.felix.delta.check.CheckInfo;
import de.felix.delta.check.CheckType;
import de.felix.delta.data.DataHolder;
import de.felix.delta.util.Point;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

@CheckInfo(checkName = "RotationPattern", checkType = CheckType.ROTATION, punishable = false)
public class MousePattern extends Check {

    private final Map<UUID, List<Point>> mouseMoves = new HashMap<>();

    public MousePattern(DataHolder dataHolder) {
        super(dataHolder);
    }

    @Override
    public void handle(PlayerEvent packet) {
        if (packet instanceof PlayerMoveEvent) {

            if (getDataHolder().rotationData.getDeltaYaw() == 0 && getDataHolder().rotationData.getDeltaPitch() == 0)
                return;

            if (getDataHolder().getEnemyData().enemy == null) {
                mouseMoves.clear();
                return;
            }

            if (getDataHolder().enemyData.enemy == null)
                return;
            final List<Point> lastMoves = mouseMoves.getOrDefault(getPlayer().getUniqueId(), new ArrayList<>());
            final Point mouseMove = new Point(getDataHolder().rotationData.getDeltaYaw(), getDataHolder().rotationData.getDeltaPitch(), 0);

            lastMoves.add(mouseMove);

            if (lastMoves.size() > 10)
                lastMoves.remove(0);

            mouseMoves.put(getPlayer().getUniqueId(), lastMoves);

            final MouseMovementStats.Stats stats = MouseMovementStats.calculateMouseMovementStats(lastMoves);

            final double deltaSpeed = Math.abs(stats.maxSpeed - stats.minSpeed);

            if (deltaSpeed >= 360) {
                Bukkit.broadcastMessage(stats.maxAngle + "");
            }

        }
    }
}
