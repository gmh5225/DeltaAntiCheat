package de.felix.delta.check;

import de.felix.delta.check.alert.AlertBuilder;
import de.felix.delta.data.DataHolder;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

@Getter
public abstract class Check {

    private final String checkName;
    private final CheckType checkType;
    private final boolean punishable;

    @Getter
    private final Player player;
    @Getter
    private final DataHolder dataHolder;

    @Getter
    private final AlertBuilder alertBuilder;

    public Check(final DataHolder dataHolder) {
        final CheckInfo checkInfo = this.getClass().getAnnotation(CheckInfo.class);
        this.checkName = checkInfo.checkName();
        this.checkType = checkInfo.checkType();
        this.punishable = checkInfo.punishable();
        this.dataHolder = dataHolder;
        this.player = dataHolder.player;
        this.alertBuilder = new AlertBuilder(this);
    }

    public abstract void handle(PlayerEvent packet);

}
