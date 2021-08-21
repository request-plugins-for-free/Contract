package me.tofpu.contract.user.service;

import me.tofpu.contract.user.User;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    /**
     * @param user adds the user to the list
     */
    User registerUser(final User user);

    /**
     * @param uniqueId the user unique id
     *
     * @return the user instance, if it's available
     */
    Optional<User> getUser(final UUID uniqueId);

    /**
     * @param name the user name
     *
     * @return the user instance, if it's available
     */
    Optional<User> getUser(final String name);

    /**
     * @param uniqueId the user unique id
     *
     * @return the user instance or a brand new instance if it's not available
     */
    User getUserOrDefault(final UUID uniqueId);

    /**
     * This will remove the user associated with the same unique id
     *
     * @param uniqueId user unique id
     */
    void removeUser(final UUID uniqueId);

    /**
     * Saves all the users to that specific directory
     *
     * @param directory the directory to save all the users data
     */
    void saveAll(final File directory);
}
