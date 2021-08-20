package me.tofpu.contract.user.impl;

import com.google.common.collect.Lists;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.user.User;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class UserImpl implements User {
    private String name;
    private final UUID uniqueId;
    private Contract currentContract;
    private double totalRating;

    public UserImpl(final UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public UserImpl(
            final String name,
            final UUID uniqueId,
            final Contract currentContract,
            final double totalRating) {
        this.name = name;
        this.uniqueId = uniqueId;
        this.currentContract = currentContract;
        this.totalRating = totalRating;
    }

    /**
     * @return the user name
     */
    @Override
    public String name() {
        return name;
    }

    /**
     * @param newName the player name
     */
    @Override
    public void name(final String newName) {
        this.name = newName;
    }

    /**
     * @return the user unique id
     */
    @Override
    public UUID uniqueId() {
        return uniqueId;
    }

    /**
     * @return the user current contract, if it's available
     */
    @Override
    public Optional<Contract> currentContract() {
        return Optional.ofNullable(currentContract);
    }

    /**
     * @param currentContract the current contract the user is in
     */
    @Override
    public void currentContract(final Contract currentContract) {
        this.currentContract = currentContract;
    }

    /**
     * @return the user total rating
     */
    @Override
    public double totalRating() {
        // FIXME: 8/20/2021 
        return totalRating;
    }

    /**
     * @return the user average rating
     */
    @Override
    public double averageRating() {
        // TODO: GOOGLE HOW TO GET AVERAGE NUMBER, ETC
        return 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof UserImpl)) return false;
        final UserImpl user = (UserImpl) o;
        return uniqueId().equals(user.uniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueId());
    }
}
