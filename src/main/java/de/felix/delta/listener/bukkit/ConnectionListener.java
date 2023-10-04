package de.felix.delta.listener.bukkit;

import de.felix.delta.DeltaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        DeltaPlugin.getInstance().dataManager.createDataObject(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        DeltaPlugin.getInstance().dataManager.removeDataObject(e.getPlayer().getUniqueId());
    }
}
