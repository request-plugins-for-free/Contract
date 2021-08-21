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
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        commandManager.getCommandCompletions().registerCompletion("contractsId", context -> contractsId(context.getPlayer(), false));
        commandManager.getCommandCompletions().registerCompletion("contractsEnded", context -> contractsId(context.getPlayer(), true));

        commandManager.getCommandContexts().registerContext(User.class, context -> {
            final Optional<User> user = userService.getUser(context.getPlayer().getUniqueId());
            return user.orElseGet(() -> {
                final String arg = context.getFirstArg();
                if (arg == null) return null;
                return userService.getUser(arg).orElse(null);
            });
        });

        commandManager.getCommandContexts().registerContext(Confirmation.class, context -> {
            User employer = null;
            for (final Map.Entry<String, Object> string : context.getPassedArgs().entrySet()){
                if (string.getKey().equals("employer")){
                    employer = (User) string.getValue();
                    break;
                }
            }
            if (employer == null) return null;
            return ConfirmationRegistry.getConfirmationManager().get(employer.uniqueId()).orElse(null);
        });

        // command registrations
        commandManager.registerCommand(new MainCommand(userService, contractService));
    }
}
