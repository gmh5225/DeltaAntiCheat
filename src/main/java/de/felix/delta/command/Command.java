package de.felix.delta.command;

import lombok.Getter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;



public abstract class Command implements CommandExecutor {
    public abstract void onCommand(CommandSender sender, String[] args);
}
