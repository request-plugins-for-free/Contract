package me.tofpu.contract.user;

import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.user.properties.stars.Stars;

import java.util.Optional;
import java.util.UUID;

public interface User extends Stars {

    /**
     * @return the user name
     */
    String name();

    /**
     * @param newName the player name
     */
    void name(final String newName);

    /**
     * @return the user unique id
     */
    UUID uniqueId();

    /**
     * @return the user current contract, if it's available
     */
    Optional<Contract> currentContract();

    /**
     * @param currentContract the current contract the user is in
     */
    void currentContract(final Contract currentContract);
}
