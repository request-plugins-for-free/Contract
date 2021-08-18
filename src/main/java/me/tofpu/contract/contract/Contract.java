package me.tofpu.contract.contract;

import java.time.Duration;
import java.util.UUID;

public interface Contract {

    /**
     * @return the employer (whom created the contract)
     */
    UUID getEmployerId();

    /**
     * @return the contractor (whom accepted the contract)
     */
    UUID getContractorId();

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
