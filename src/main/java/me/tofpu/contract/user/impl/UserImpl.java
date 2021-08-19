package me.tofpu.contract.user.impl;

import com.google.common.collect.Lists;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.properties.stars.review.UserReview;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class UserImpl implements User {
    private String name;
    private final UUID uniqueId;
    private Contract currentContract;
    private double totalRating;
    private final List<UserReview> ratedBy;

    public UserImpl(final UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.ratedBy = Lists.newArrayList();
    }

    public UserImpl(final UUID uniqueId, final List<UserReview> ratedBy) {
        this.uniqueId = uniqueId;
        this.ratedBy = ratedBy;
    }

    public UserImpl(
            final String name,
            final UUID uniqueId,
            final Contract currentContract,
            final double totalRating,
            final List<UserReview> ratedBy) {
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
     * @param contract the current contract the user is in
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
     * @return list of reviews whom rated this user
     */
    @Override
    public List<UserReview> ratedBy() {
        return ratedBy;
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
