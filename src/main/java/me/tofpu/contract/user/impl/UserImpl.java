package me.tofpu.contract.user.impl;

import com.google.common.collect.Lists;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.user.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class UserImpl implements User {
    private String name;
    private final UUID uniqueId;
    private Contract currentContract;
    private int totalRating;
    private final List<User> ratedBy;

    public UserImpl(final UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.ratedBy = Lists.newArrayList();
    }

    public UserImpl(final UUID uniqueId, final List<User> ratedBy) {
        this.uniqueId = uniqueId;
        this.ratedBy = ratedBy;
    }

    public UserImpl(
            final String name,
            final UUID uniqueId,
            final Contract currentContract,
            final int totalRating,
            final List<User> ratedBy) {
        this.name = name;
        this.uniqueId = uniqueId;
        this.currentContract = currentContract;
        this.totalRating = totalRating;
        this.ratedBy = ratedBy;
    }

    /**
     * @return the user name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name the player name
     */
    @Override
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the user unique id
     */
    @Override
    public UUID getUniqueId() {
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
     * @return the user total rating
     */
    @Override
    public double totalRating() {
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

    /**
     * @return list of users whom rated this user
     */
    @Override
    public List<User> ratedBy() {
        return ratedBy;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof UserImpl)) return false;
        final UserImpl user = (UserImpl) o;
        return getUniqueId().equals(user.getUniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUniqueId());
    }
}
