package me.tofpu.contract.data;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.adapter.ContractAdapter;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.adapter.UserAdapter;
import me.tofpu.contract.user.factory.UserFactory;
import me.tofpu.contract.user.service.UserService;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class DataManager {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(User.class, new UserAdapter())
            .registerTypeAdapter(Contract.class, new ContractAdapter())
            .create();

    private final UserService userService;
    private final ContractService contractService;

    private final File[] files;

    public DataManager(final UserService userService, final ContractService contractService) {
        this.userService = userService;
        this.contractService = contractService;
        this.files = new File[2];
    }

    public void initialize(final File directory){
        this.files[0] = new File(directory, "users");
        this.files[1] = new File(directory, "contracts");
    }

    public void load(){
        contractService.loadAll(files[1]);
    }

    public void save(){
        contractService.saveAll(files[1]);
        userService.saveAll(files[1]);
    }

    public Optional<User> loadUser(final Player player) {
        final File file = new File(files[0], player.getUniqueId().toString());
        if (!file.exists()) return Optional.of(getAndRegisterUser(player));

        try (final FileReader reader = new FileReader(file)) {
            final User user = GSON.fromJson(reader, User.class);
            if (user == null) return Optional.of(getAndRegisterUser(player));

            userService.registerUser(user);
            return Optional.of(user);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private User getAndRegisterUser(final Player player){
        final User user = UserFactory.create(player.getName(), player.getUniqueId(), Lists.newArrayList());
        userService.registerUser(user);

        return user;
    }

    public UserService getUserService() {
        return userService;
    }
}
