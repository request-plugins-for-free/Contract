package me.tofpu.contract.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.adapter.ContractAdapter;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.adapter.UserAdapter;
import me.tofpu.contract.user.service.UserService;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class DataManager {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(User.class, new UserAdapter())
            .registerTypeAdapter(Contract.class, new ContractAdapter())
            .create();
    private final UserService userService;
    private final File[] files;

    public DataManager(final UserService userService) {
        this.userService = userService;
        this.files = new File[3];
    }

    public void initialize(final File directory){
        this.files[0] = new File(directory, "users");
    }

    public Optional<User> loadUser(final UUID uniqueId) {
        final File file = new File(files[0], uniqueId.toString());
        if (!file.exists()) return Optional.empty();
        try (final FileReader reader = new FileReader(file)) {
            final User user = GSON.fromJson(reader, User.class);
            userService.registerUser(user);
            return Optional.ofNullable(user);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public UserService getUserService() {
        return userService;
    }
}
