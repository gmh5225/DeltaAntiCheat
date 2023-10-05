package de.felix.delta.check.alert;

import de.felix.delta.check.Check;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AlertBuilder {
    public Player player;

    private String alertMessage;

    public Check check;

    public AlertBuilder(final Check check) {
        this.check = check;
        this.player = check.getPlayer();
    }

    public AlertBuilder setAlertMessage(final String alertMessage) {
        this.alertMessage = alertMessage;
        return this;
    }

    public AlertBuilder runAlert(AlertTagBuilder alertTagBuilder) {
        if (alertMessage.isEmpty()) return this;

        else if (player.isOp() || player.hasPermission("delta.alerts")) {
            if (check.isPunishable())
                punish();
            player.sendMessage(ChatColor.GRAY + "[" + ChatColor.RED + "Δ - delta" + ChatColor.GRAY + "] " + ChatColor.GRAY + alertMessage + " " + alertTagBuilder.toString());
        }
        return this;
    }

    private void punish() {

    }

}
