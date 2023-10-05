package de.felix.delta.listener;

import cc.funkemunky.api.utils.handlers.PlayerSizeHandler;
import de.felix.delta.DeltaPlugin;
import de.felix.delta.data.DataHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

public class DataListener implements Listener {


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        final DataHolder dataHolder = DeltaPlugin.getInstance().dataManager.getDataHolder(event.getPlayer());
        if (dataHolder == null) return;
        final Vector move = new Vector(event.getTo().getX(), event.getTo().getY(), event.getTo().getZ());
        dataHolder.movementData.process(move);

        dataHolder.box = PlayerSizeHandler.instance.bounds(dataHolder.player.getPlayer(),event.getTo().getX(), event.getTo().getY(), event.getTo().getZ());

        dataHolder.worldData.runCollisionCheck();

        DeltaPlugin.getInstance().checkManager.getChecks(dataHolder).forEach(check -> check.handle(event));
        //todo can be helpful
        // Bukkit.broadcastMessage(String.valueOf(dataHolder.movementData.movementStorage.getCurrentPosition().getY() - dataHolder.movementData.movementStorage.getPointBehindTick(0, false).getY()));
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        final DataHolder dataHolder = DeltaPlugin.getInstance().dataManager.getDataHolder(event.getPlayer());
        if (dataHolder == null) return;

        dataHolder.teleportData.setLastTeleport(event.getTo().clone());
        dataHolder.teleportData.lastTeleportTime = System.currentTimeMillis();
        DeltaPlugin.getInstance().checkManager.getChecks(dataHolder).forEach(check -> check.handle(event));
    }
}