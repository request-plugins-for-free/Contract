package me.tofpu.contract.user.service.impl;

import com.google.common.collect.Lists;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.factory.UserFactory;
import me.tofpu.contract.user.service.UserService;

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
            if (user.getUniqueId().equals(uniqueId)) return Optional.of(user);
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

    private void addUser(final User user){
        this.users.add(user);
    }
}
