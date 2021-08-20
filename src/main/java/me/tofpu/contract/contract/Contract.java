package me.tofpu.contract.contract;

import me.tofpu.contract.contract.review.ContractReview;

import java.time.Duration;
import java.util.UUID;

public interface Contract {

    /**
     * @return the employer name (whom created the contract)
     */
    String employerName();

    /**
     * @return the employer unique id (whom created the contract)
     */
    UUID employerId();

    /**
     * @return the contractor name (whom accepted the contract)
     */
    String contractorName();

    /**
     * @return the contractor unique id (whom accepted the contract)
     */
    UUID contractorId();


    /**
     * @param newName new employer name
     */
    void employerName(final String newName);


    /**
     * @param newName new contractor name
     */
    void contractorName(final String newName);

    /**
     * @return the contract review instance
     */
    ContractReview review();

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
    double amount();

    /**
     * @return the contract description
     */
    String description();

    /**
     * @return returns true if the contract duration has reached the contract length, otherwise false
     */
    boolean hasEnded();

    UUID id();
}
