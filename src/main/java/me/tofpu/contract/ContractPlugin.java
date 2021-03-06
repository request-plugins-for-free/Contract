package me.tofpu.contract;

import com.github.requestpluginsforfree.config.ConfigAPI;
import com.github.requestpluginsforfree.config.type.identifier.ConfigIdentifier;
import com.github.requestpluginsforfree.dependency.Dependency;
import com.github.requestpluginsforfree.dependency.api.DependencyAPI;
import com.github.requestpluginsforfree.dependency.impl.PlaceholderDependency;
import com.github.requestpluginsforfree.dependency.impl.VaultDependency;
import me.tofpu.contract.command.CommandHandler;
import me.tofpu.contract.contract.runnable.ContractRunnable;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.contract.service.impl.ContractServiceImpl;
import me.tofpu.contract.data.DataManager;
import me.tofpu.contract.data.listener.PlayerJoinListener;
import me.tofpu.contract.data.listener.PlayerQuitListener;
import me.tofpu.contract.expansion.ContractExpansion;
import me.tofpu.contract.listener.AsyncPlayerChat;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.factory.UserFactory;
import me.tofpu.contract.user.impl.UserImpl;
import me.tofpu.contract.user.service.UserService;
import me.tofpu.contract.user.service.impl.UserServiceImpl;
import me.tofpu.contract.util.confirmation.manager.ConfirmationRegistry;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

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

    private void initializes() {
        initializeDependency();
        initializeData();
        initializeCommand();

        ConfigAPI.initialize(ConfigIdentifier.of("config", getConfig()), new ConfigIdentifier("messages", dataManager.getPluginFiles()[0].configuration()));

        UserFactory.initialize(userService);
        ContractRunnable.initialize(userService, economy);
        UserImpl.initialize(contractService);
        ConfirmationRegistry.initialize(this, userService);
    }

    private void initializeData() {
        this.dataManager.initialize(this, getDataFolder());
    }

    private void initializeCommand() {
        new CommandHandler(this, economy, userService, contractService).initialize();
    }

    private void initializeDependency() {
        DependencyAPI.register(new VaultDependency());
        DependencyAPI.register(new PlaceholderDependency());

        DependencyAPI.initialize(this);
        final Dependency<?> placeholderAPI = DependencyAPI.get("PlaceholderAPI");
        if (placeholderAPI != null && placeholderAPI.isAvailable()){
            new ContractExpansion(getDescription(), userService);
        }

        final Dependency<?> vault = DependencyAPI.get("Vault");
        if (vault != null && vault.isAvailable()) {
            this.economy = (Economy) vault.getInstance();
        }
    }

    private void initializeListeners() {
        final PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new PlayerJoinListener(dataManager), this);
        manager.registerEvents(new PlayerQuitListener(dataManager), this);
        manager.registerEvents(new AsyncPlayerChat(userService), this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        initializes();
        initializeListeners();

        dataManager.load();

        // reload patch
        Bukkit.getOnlinePlayers().forEach(player -> {
            final Optional<User> user = this.dataManager.loadUser(player);

            user.ifPresent(value -> value.name(player.getName()));
        });
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
