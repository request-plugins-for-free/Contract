package me.tofpu.contract.user.properties.stars.review.factory;

import me.tofpu.contract.user.properties.stars.review.UserReview;
import me.tofpu.contract.user.properties.stars.review.impl.UserReviewImpl;

import java.util.UUID;

public class UserReviewFactory {
    public static UserReview create(final String name, final UUID uniqueId, final double rated, final String description){
        return new UserReviewImpl(name, uniqueId, rated, description);
    }
}
