package me.tofpu.contract.contract.impl;

import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.user.User;

import java.time.Duration;
import java.util.Objects;

public class ContractImpl implements Contract {
    private final User employerId;
    private final User contractorId;

    private final long startedAt;
    private final long length;
    private final double amount;
    private final String description;

    public ContractImpl(final User employerId, final User contractorId, final long length, final double amount, final String description) {
        this.employerId = employerId;
        this.contractorId = contractorId;
        this.description = description;
        this.startedAt = System.nanoTime();
        this.length = length;
        this.amount = amount;
    }

    /**
     * @return the employer (whom created the contract)
     */
    @Override
    public User getEmployer() {
        return employerId;
    }

    /**
     * @return the contractor (whom accepted the contract)
     */
    @Override
    public User getContractor() {
        return contractorId;
    }

    /**
     * @return the duration of the contract
     */
    @Override
    public Duration getDuration() {
        return Duration.ofNanos(startedAt() - System.nanoTime());
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
    public double getAmount() {
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
        return duration.toMinutes() >= length();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ContractImpl)) return false;
        final ContractImpl contract = (ContractImpl) o;
        return getEmployer().equals(contract.getEmployer()) && getContractor().equals(contract.getContractor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmployer(), getContractor());
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ContractImpl{");
        sb.append("employerId=").append(employerId);
        sb.append(", contractorId=").append(contractorId);
        sb.append(", startedAt=").append(startedAt);
        sb.append(", length=").append(length);
        sb.append(", amount=").append(amount);
        sb.append('}');
        return sb.toString();
    }
}
