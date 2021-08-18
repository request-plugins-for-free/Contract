package me.tofpu.contract.user.properties.stars;

import me.tofpu.contract.user.properties.stars.review.UserReview;

import java.util.List;

public interface Stars {

    /**
     * @return the user total rating
     */
    double totalRating();

    /**
     * @return the user average rating
     */
    double averageRating();

    /**
     * @return list of reviews whom rated this user
     */
    List<UserReview> ratedBy();
}
