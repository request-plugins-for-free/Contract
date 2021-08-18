package me.tofpu.contract.user.properties.stars.review.impl;

import me.tofpu.contract.user.properties.stars.review.UserReview;

import java.util.UUID;

public class UserReviewImpl implements UserReview {
    private final String name;
    private final UUID uniqueId;
    private final double rated;
    private final String description;

    public UserReviewImpl(final String name, final UUID uniqueId, final double rated, final String description) {
        this.name = name;
        this.uniqueId = uniqueId;
        this.rated = rated;
        this.description = description;
    }

    /**
     * @return the reviewer
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * @return the unique id of reviewer
     */
    @Override
    public UUID uniqueId() {
        return this.uniqueId;
    }

    /**
     * @return the rating of the reviewer
     */
    @Override
    public double rated() {
        return this.rated;
    }

    /**
     * @return the description of the rating whom the reviewer wrote
     */
    @Override
    public String description() {
        return this.description;
    }
}
