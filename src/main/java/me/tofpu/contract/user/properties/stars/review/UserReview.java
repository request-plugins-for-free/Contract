package me.tofpu.contract.user.properties.stars.review;

import java.util.UUID;

public interface UserReview {

    /**
     * @return the reviewer
     */
    String getName();

    /**
     * @return the unique id of reviewer
     */
    UUID uniqueId();

    /**
     * @return the rating of the reviewer
     */
    double rated();

    /**
     * @return the description of the rating whom the reviewer wrote
     */
    String description();
}
