package me.tofpu.contract.data;

import com.github.requestpluginsforfree.fileutil.file.PluginFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.adapter.ContractAdapter;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.data.file.MessageFile;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.adapter.UserAdapter;
import me.tofpu.contract.user.factory.UserFactory;
import me.tofpu.contract.user.service.UserService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class DataManager {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().registerTypeAdapter(User.class, new UserAdapter()).registerTypeAdapter(Contract.class, new ContractAdapter()).create();

    private final UserService userService;
    private final ContractService contractService;

    private final File[] files;
    private final PluginFile[] pluginFiles;

    public DataManager(final UserService userService, final ContractService contractService) {
        this.userService = userService;
        this.contractService = contractService;
        this.files = new File[2];
        this.pluginFiles = new PluginFile[1];
    }

    public void initialize(final Plugin plugin, final File directory) {
        this.files[0] = new File(directory, "users");
        this.files[1] = new File(directory, "contracts");

        this.pluginFiles[0] = new MessageFile(plugin, directory);
        for (final File file : files) {
            if (!file.exists()) file.mkdirs();
        }

        for (final PluginFile file : pluginFiles){
            file.initialize(false);
        }
    }

    public void load() {
        contractService.loadAll(files[1]);
    }

    public void save() {
        contractService.saveAll(files[1]);
        //        userService.saveAll(files[0]);
        Bukkit.getServer().getOnlinePlayers().forEach(this::saveUser);
    }

    public Optional<User> loadUser(final Player player) {
        final File file = new File(files[0], player.getUniqueId() + ".json");
        if (!file.exists()) return Optional.of(getAndRegisterUser(player));

        try (final FileReader reader = new FileReader(file)) {
            final User user = GSON.fromJson(reader, User.class);
            if (user == null) return Optional.of(getAndRegisterUser(player));

            // "TRIES" to unfreeze the current contract since they joined back in
            user.currentContract().ifPresent(contract -> contract.freeze(false));

            return Optional.of(userService.registerUser(user));
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void saveUser(final Player player) {
        final File file = new File(files[0], player.getUniqueId() + ".json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (final FileWriter writer = new FileWriter(file)) {
            final Optional<User> user = userService.getUser(player.getUniqueId());
            if (!user.isPresent()) return;
            user.get().currentContract().ifPresent(contract -> contract.freeze(true));

            writer.write(GSON.toJson(user.get(), User.class));
            userService.removeUser(player.getUniqueId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User getAndRegisterUser(final Player player) {
        return userService.registerUser(UserFactory.create(player.getName(), player.getUniqueId()));
    }

    public UserService getUserService() {
        return userService;
    }

    public PluginFile[] getPluginFiles() {
        return pluginFiles;
    }
}
