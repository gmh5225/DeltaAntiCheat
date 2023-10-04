package de.felix.delta.event;


import cc.funkemunky.api.events.AtlasEvent;
import cc.funkemunky.api.events.Cancellable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@Setter
public class MoveEvent extends AtlasEvent implements Cancellable {
    private Player player;
    private Location to, from;
    private boolean cancelled;

    public MoveEvent(Player player, Location to, Location from) {
        this.player = player;
        this.to = to;
        this.from = from;
    }
}