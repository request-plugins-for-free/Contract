package me.tofpu.contract.contract.impl;

import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.review.ContractReview;
import me.tofpu.contract.contract.runnable.ContractRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

public class ContractImpl implements Contract {
    private final UUID id;
    private final ContractRunnable runnable;

    private final UUID employerId;
    private final UUID contractorId;

    private final ContractReview review;

    private long startedAt;
    private final double amount;

    private final String description;
    private String employerName;
    private String contractorName;
    private boolean frozen;
    private long length;

    public ContractImpl(final UUID id, final String employerName, final UUID employerId, final String contractorName, final UUID contractorId, final ContractReview review, final String description, final long length, final double amount) {
        this.id = id;
        this.frozen = true;

        this.employerName = employerName;
        this.employerId = employerId;

        this.contractorName = contractorName;
        this.contractorId = contractorId;

        this.review = review;

        this.description = description;
        this.length = length;
        this.amount = amount;

        this.runnable = new ContractRunnable(this);
    }

    /**
     * @return the employer name (whom created the contract)
     */
    @Override
    public String employerName() {
        return employerName;
    }

    /**
     * @return the employer unique id (whom created the contract)
     */
    @Override
    public UUID employerId() {
        return employerId;
    }

    /**
     * @return the contractor name (whom accepted the contract)
     */
    @Override
    public String contractorName() {
        return contractorName;
    }

    /**
     * @return the contractor unique id (whom accepted the contract)
     */
    @Override
    public UUID contractorId() {
        return contractorId;
    }

    /**
     * @param newName new employer name
     */
    @Override
    public void employerName(final String newName) {
        this.employerName = newName;
    }

    /**
     * @param newName new contractor name
     */
    @Override
    public void contractorName(final String newName) {
        this.contractorName = newName;
    }

    /**
     * returns true if the timer stopped else, returns false if the timer is ticking
     *
     * @return the freeze status
     */
    public boolean freeze() {
        return this.frozen;
    }

    /**
     * this will freeze/unfreeze the timer depending on the status parameter
     *
     * @param status the freeze status, false to have it ticking, true to freeze it
     */
    public void freeze(final boolean status) {
        if (status) {
            this.length -= getDuration().getSeconds();
            this.runnable.cancel();
        } else {
            // if it was frozen, set the startedAt to now
            if (this.frozen) {
                this.startedAt = System.nanoTime();
            }
            // trying to get te employer & contractor player instance
            final Player employer = Bukkit.getPlayer(employerId);
            final Player contractor = Bukkit.getPlayer(contractorId);

            // if both the employer & contractor are online, ladies and gentlemen, let the timer tickin'!
            if (employer != null && contractor != null) this.runnable.start();
        }
        this.frozen = status;
    }

    /**
     * @return the contract review instance
     */
    @Override
    public ContractReview review() {
        return review;
    }

    /**
     * @return the duration of the contract
     */
    @Override
    public Duration getDuration() {
        return Duration.ofNanos(System.nanoTime() - startedAt());
    }

    /**
     * @return when the contract started
     */
    @Override
    public long startedAt() {
        return this.startedAt;
    }

    /**
     * @return the contract length
     */
    @Override
    public long length() {
        return this.length;
    }

    /**
     * @return the contract amount (when it's successful)
     */
    @Override
    public double amount() {
        return this.amount;
    }

    /**
     * @return the contract description
     */
    @Override
    public String description() {
        return this.description;
    }

    /**
     * @return returns true if the contract duration has reached the contract length, otherwise false
     */
    @Override
    public boolean hasEnded() {
        final Duration duration = getDuration();
        final long seconds = duration.getSeconds();
        return seconds >= length();
    }

    @Override
    public UUID id() {
        return this.id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ContractImpl)) return false;
        final ContractImpl contract = (ContractImpl) o;
        return employerId.equals(contract.employerId) && contractorId.equals(contract.contractorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employerId, contractorId);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ContractImpl{");
        sb.append("id=").append(id);
        sb.append(", runnable=").append(runnable);
        sb.append(", employerId=").append(employerId);
        sb.append(", contractorId=").append(contractorId);
        sb.append(", review=").append(review);
        sb.append(", startedAt=").append(startedAt);
        sb.append(", amount=").append(amount);
        sb.append(", description='").append(description).append('\'');
        sb.append(", employerName='").append(employerName).append('\'');
        sb.append(", contractorName='").append(contractorName).append('\'');
        sb.append(", frozen=").append(frozen);
        sb.append(", length=").append(length);
        sb.append('}');
        return sb.toString();
    }
}
