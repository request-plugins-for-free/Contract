package me.tofpu.contract;

import me.tofpu.contract.command.CommandHandler;
import me.tofpu.contract.contract.factory.ContractFactory;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.contract.service.impl.ContractServiceImpl;
import me.tofpu.contract.data.DataManager;
import me.tofpu.contract.user.factory.UserFactory;
import me.tofpu.contract.user.listener.PlayerJoinListener;
import me.tofpu.contract.user.service.UserService;
import me.tofpu.contract.user.service.impl.UserServiceImpl;
import org.bukkit.plugin.java.JavaPlugin;

public final class ContractPlugin extends JavaPlugin {
    private final UserService userService;
    private final ContractService contractService;

    private final DataManager dataManager;

    public ContractPlugin() {
        this.userService = new UserServiceImpl();
        this.contractService = new ContractServiceImpl();

        this.dataManager = new DataManager(userService, contractService);
    }

    private void initializeFactories(){
        UserFactory.initialize(userService);
        ContractFactory.initialize(contractService, userService, dataManager);

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(dataManager), this);
    }

    private void initializeData(){
        this.dataManager.initialize(getDataFolder());
    }

    private void initializeCommand(){
        new CommandHandler(this, userService, contractService);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        initializeFactories();
        initializeData();
        initializeCommand();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
