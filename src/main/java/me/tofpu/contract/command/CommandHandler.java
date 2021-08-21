package me.tofpu.contract.command;

import co.aikar.commands.BukkitCommandManager;
import com.google.common.collect.Lists;
import me.tofpu.contract.command.extend.MainCommand;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.user.service.UserService;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class CommandHandler {
    private final BukkitCommandManager commandManager;

    private final UserService userService;
    private final ContractService contractService;

    public CommandHandler(final Plugin plugin, final UserService userService, final ContractService contractService) {
        this.commandManager = new BukkitCommandManager(plugin);
        this.userService = userService;
        this.contractService = contractService;
    }

    private List<String> contractsId(final Player player, boolean hasFinished) {
        final List<String> ids = Lists.newArrayList();

        for (final Contract contract : contractService.of(player.getUniqueId())) {
            if (hasFinished) {
                if (contract.hasEnded()) ids.add(contract.id().toString());
            } else ids.add(contract.id().toString());
        }

        return ids;
    }

    public void initialize() {
        // command completions
        commandManager.getCommandCompletions().registerCompletion("contractsId", context -> {
            return contractsId(context.getPlayer(), false);
        });

        commandManager.getCommandCompletions().registerCompletion("contractsEnded", context -> {
            return contractsId(context.getPlayer(), true);
        });

        // command registrations
        commandManager.registerCommand(new MainCommand(userService, contractService));
    }
}
