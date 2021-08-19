package me.tofpu.contract.user.service.impl;

import com.google.common.collect.Lists;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.data.DataManager;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.factory.UserFactory;
import me.tofpu.contract.user.service.UserService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    private final List<User> users;

    public UserServiceImpl() {
        users = Lists.newArrayList();
    }

    /**
     * @param user adds the user to the list
     */
    @Override
    public User registerUser(final User user) {
        addUser(user);
        return user;
    }

    /**
     * @param uniqueId the user unique id
     *
     * @return the user instance, if it's available
     */
    @Override
    public Optional<User> getUser(final UUID uniqueId) {
        for (final User user : this.users){
            if (user.uniqueId().equals(uniqueId)) return Optional.of(user);
        }
        return Optional.empty();
    }

    /**
     * @param uniqueId the user unique id
     *
     * @return the user instance or a brand new instance if it's not available
     */
    @Override
    public User getUserOrDefault(final UUID uniqueId) {
        final Optional<User> user = getUser(uniqueId);

        return user.orElseGet(() -> registerUser(UserFactory.create(uniqueId)));
    }

    /**
     * Saves all the users to that specific directory
     *
     * @param directory the directory to save all the users data
     */
    @Override
    public void saveAll(final File directory) {
        for (final User user : this.users){
            final File file = new File(directory, user.name());
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try (final FileWriter writer = new FileWriter(file)){
                writer.write(DataManager.GSON.toJson(user, User.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addUser(final User user){
        this.users.add(user);
    }
}
