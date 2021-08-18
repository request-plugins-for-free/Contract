package me.tofpu.contract;

import me.tofpu.contract.contract.factory.ContractFactory;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.contract.service.impl.ContractServiceImpl;
import me.tofpu.contract.user.factory.UserFactory;
import me.tofpu.contract.user.service.UserService;
import me.tofpu.contract.user.service.impl.UserServiceImpl;
import org.bukkit.plugin.java.JavaPlugin;

public final class ContractPlugin extends JavaPlugin {
    private final UserService userService;
    private final ContractService contractService;

    public ContractPlugin() {
        this.userService = new UserServiceImpl();
        this.contractService = new ContractServiceImpl();
    }

    private void initializeFactories(){
        UserFactory.initialize(userService);
        ContractFactory.initialize(contractService, userService);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        initializeFactories();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
