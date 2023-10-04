package de.felix.delta.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.felix.delta.command.commands.HelpCommand;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EnumChatFormat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "Delta", description = "Main command", usage = "/<command> [subcommand]")
public class DeltaCommand implements CommandExecutor {
    @Getter
    private final Map<String, CommandExecutor> subCommands = new HashMap<>();

    public DeltaCommand() {
        subCommands.put("help", new HelpCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            CommandExecutor subCommand = subCommands.get(args[0].toLowerCase());
            if (subCommand != null) {
                return subCommand.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            }
        }

        sender.sendMessage("------------ " + EnumChatFormat.GREEN + "Delta" + EnumChatFormat.GRAY + " ------------");
        sender.sendMessage(EnumChatFormat.GRAY + "Created by " + EnumChatFormat.GREEN + "Felix1337");
        sender.sendMessage(EnumChatFormat.GRAY + "Version: " + EnumChatFormat.GREEN + "1.0");
        sender.sendMessage(EnumChatFormat.GRAY + "Type " + EnumChatFormat.GREEN + "/Delta help" + EnumChatFormat.GRAY + " for help.");
        sender.sendMessage("------------ " + EnumChatFormat.GREEN + "Delta" + EnumChatFormat.GRAY + " ------------");
        return true;
    }
}