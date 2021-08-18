package me.tofpu.contract.contract;

import me.tofpu.contract.user.User;

import java.time.Duration;

public interface Contract {

    /**
     * @return the employer (whom created the contract)
     */
    User getEmployer();

    /**
     * @return the contractor (whom accepted the contract)
     */
    User getContractor();

    /**
     * @return the duration of the contract
     */
    Duration getDuration();

    /**
     * @return when the contract started
     */
    long startedAt();

    /**
     * @return the contract length
     */
    long length();

    /**
     * @return the contract amount (when it's successful)
     */
    double getAmount();

    /**
     * @return returns true if the contract duration has reached the contract length, otherwise false
     */
    boolean hasEnded();
}
