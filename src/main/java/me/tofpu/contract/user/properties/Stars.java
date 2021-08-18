package me.tofpu.contract.user.properties;

import me.tofpu.contract.user.User;

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
     * @return list of users whom rated this user
     */
    List<User> ratedBy();
}
