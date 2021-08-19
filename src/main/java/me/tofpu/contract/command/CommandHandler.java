package me.tofpu.contract.command;

import co.aikar.commands.BukkitCommandManager;
import me.tofpu.contract.command.extend.MainCommand;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.user.service.UserService;
import org.bukkit.plugin.Plugin;

public class CommandHandler {
    private final BukkitCommandManager commandManager;

    private final UserService userService;
    private final ContractService contractService;

    public CommandHandler(final Plugin plugin, final UserService userService, final ContractService contractService){
        this.commandManager = new BukkitCommandManager(plugin);
        this.userService = userService;
        this.contractService = contractService;
    }

    public void initialize(){
        commandManager.registerCommand(new MainCommand(userService, contractService));
    }
}
