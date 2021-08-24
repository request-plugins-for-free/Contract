package me.tofpu.contract.command;

import co.aikar.commands.BukkitCommandManager;
import com.google.common.collect.Lists;
import me.tofpu.contract.command.extend.MainCommand;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.service.UserService;
import me.tofpu.contract.util.confirmation.Confirmation;
import me.tofpu.contract.util.confirmation.manager.ConfirmationRegistry;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class CommandHandler {
    private final BukkitCommandManager commandManager;

    private final Economy economy;
    private final UserService userService;
    private final ContractService contractService;

    public CommandHandler(final Plugin plugin, final Economy economy, final UserService userService, final ContractService contractService) {
        this.commandManager = new BukkitCommandManager(plugin);
        this.economy = economy;
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
        commandManager.getCommandCompletions().registerCompletion("contractsId", context -> contractsId(context.getPlayer(), false));
        commandManager.getCommandCompletions().registerCompletion("contractsEnded", context -> contractsId(context.getPlayer(), true));

        commandManager.getCommandContexts().registerIssuerAwareContext(User.class, context -> {
            System.out.println(context.hasFlag("self") + " | " + context.getFirstArg());
            if (context.hasFlag("self"))
                return userService.getUser(context.getPlayer().getUniqueId()).orElse(null);
            else {
                final String arg = context.popFirstArg();
                return userService.getUser(arg).orElse(null);
            }
        });

        commandManager.getCommandContexts().registerIssuerAwareContext(Confirmation.class, context -> ConfirmationRegistry.getConfirmationManager().get(context.getPlayer().getUniqueId(), false).orElse(null));

        // c
        // ommand registrations
        commandManager.registerCommand(new MainCommand(userService, economy, contractService));
    }
}
