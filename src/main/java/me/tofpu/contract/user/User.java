package me.tofpu.contract.user;

import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.user.properties.stars.Stars;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

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

    /**
     * @return returns true if player instance exists otherwise false
     */
    boolean isPresent();

    /**
     * This will accept the consumer if player is online
     *
     * @param consumer the player consumer
     */
    void ifPresent(final Consumer<Player> consumer);

    /**
     * @return the player instance of user
     */
    Player player();
}
