package me.tofpu.contract.command.base;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.RegisteredCommand;
import me.tofpu.contract.util.Util;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class ExtraBaseCommand extends BaseCommand {
    private final String identifier;

    public ExtraBaseCommand(String identifier) {
        this.identifier = identifier;
    }

    public void onHelp(final CommandSender sender) {
        sender.sendMessage(Util.colorize("&e&l&m<&6&m------&r &e&lContract Commands &6&m------&e&l&m>"));
        for (final Map.Entry<String, RegisteredCommand> test : getSubCommands().entries()) {
            final RegisteredCommand<?> command = test.getValue();
            if (command.isPrivate()) continue;
            sender.sendMessage(Util.format(command));
        }
        sender.sendMessage(Util.colorize("&e&l&m<&r&6&m----------------&e&l&m>&r &e&l&m<&r&6&m----------------&e&l&m>"));
    }

    public void onUnknownCommand(final CommandSender sender) {
        sender.sendMessage(Util.colorize("&cUnknown command, type /" + identifier + " help for more info!"));
    }
}
