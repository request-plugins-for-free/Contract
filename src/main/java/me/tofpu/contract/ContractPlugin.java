package me.tofpu.contract;

import com.github.requestpluginsforfree.ConfigAPI;
import com.github.requestpluginsforfree.dependency.Dependency;
import com.github.requestpluginsforfree.dependency.registry.DependencyAPI;
import me.tofpu.contract.command.CommandHandler;
import me.tofpu.contract.contract.runnable.ContractRunnable;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.contract.service.impl.ContractServiceImpl;
import me.tofpu.contract.data.DataManager;
import me.tofpu.contract.data.listener.PlayerQuitListener;
import me.tofpu.contract.user.factory.UserFactory;
import me.tofpu.contract.user.listener.PlayerJoinListener;
import me.tofpu.contract.user.service.UserService;
import me.tofpu.contract.user.service.impl.UserServiceImpl;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.java.JavaPlugin;

public final class ContractPlugin extends JavaPlugin {
    private final UserService userService;
    private final ContractService contractService;

    private final DataManager dataManager;
    private Economy economy;

    public ContractPlugin() {
        this.userService = new UserServiceImpl();
        this.contractService = new ContractServiceImpl();

        this.dataManager = new DataManager(userService, contractService);
    }

    private void initializeStatic() {
        UserFactory.initialize(userService);
        ContractRunnable.initialize(userService);
        ConfigAPI.initialize(getConfig());
        DependencyAPI.initialize(this);
    }

    private void initializeData() {
        this.dataManager.initialize(getDataFolder());
    }

    private void initializeCommand() {
        new CommandHandler(this, userService, contractService).initialize();
    }

    private void initializeVault() {
        final Dependency<?> vault = DependencyAPI.get("vault");
        if (vault != null && vault.isAvailable()){
            this.economy = (Economy) vault.get();
        }
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        initializeStatic();
        initializeData();
        initializeCommand();

        initializeVault();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(dataManager), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(dataManager), this);

        dataManager.load();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        dataManager.save();
    }

    private Economy getEconomy() {
        return economy;
    }
}
