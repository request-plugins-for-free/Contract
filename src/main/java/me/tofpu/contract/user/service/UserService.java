package me.tofpu.contract.user.service;

import me.tofpu.contract.user.User;

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
     * @param uniqueId the user unique id
     *
     * @return the user instance or a brand new instance if it's not available
     */
    User getUserOrDefault(final UUID uniqueId);
}
