package me.tofpu.contract.user;

import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.user.properties.stars.Stars;

import java.util.Optional;
import java.util.UUID;

public interface User extends Stars {

    /**
     * @return the user name
     */
    String getName();

    /**
     * @param name the player name
     */
    void setName(final String name);

    /**
     * @return the user unique id
     */
    UUID getUniqueId();

    /**
     * @return the user current contract, if it's available
     */
    Optional<Contract> currentContract();
}
