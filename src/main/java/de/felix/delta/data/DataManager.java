//https://github.com/funkemunky/FunkePhase/blob/master/src/main/java/cc/funkemunky/funkephase/data/DataManager.java
package de.felix.delta.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {
    private final Map<UUID, DataHolder> dataObjects = new HashMap<>();
    public DataManager() {
        Bukkit.getOnlinePlayers().forEach(player -> dataObjects.put(player.getUniqueId(), new DataHolder(player)));
    }

    public void createDataObject(Player player) {
        synchronized (dataObjects) {
            dataObjects.put(player.getUniqueId(), new DataHolder(player));
        }
    }

    public void removeDataObject(UUID uuid) {
        synchronized (dataObjects) {
            dataObjects.remove(uuid);
        }
    }

    public void shutdown() {
        synchronized (dataObjects) {
            dataObjects.clear();
        }
    }

    public synchronized DataHolder getDataHolder(Player player) {
        return dataObjects.computeIfAbsent(player.getUniqueId(), key -> new DataHolder(player));
    }

}
