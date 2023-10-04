package de.felix.delta.command.commands;

import de.felix.delta.DeltaPlugin;
import net.minecraft.server.v1_8_R3.EnumChatFormat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(EnumChatFormat.GREEN + "Delta" + EnumChatFormat.GRAY + " Available commands:");
        DeltaPlugin.getInstance().deltaCommand.getSubCommands().forEach((name, commandExecutor) -> {
            sender.sendMessage(" - " + name + EnumChatFormat.GRAY);
        });
        return true;
    }
}