package me.tofpu.contract.contract.impl;

import me.tofpu.contract.contract.Contract;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

public class ContractImpl implements Contract {
    private String employerName;
    private final UUID employerId;

    private String contractorName;
    private final UUID contractorId;

    private final long startedAt;
    private final long length;
    private final double amount;
    private final String description;

    public ContractImpl(final String employerName, final UUID employerId, final String contractorName, final UUID contractorId, final String description, final long startedAt, final long length, final double amount) {
        this.employerName = employerName;
        this.employerId = employerId;

        this.contractorName = contractorName;
        this.contractorId = contractorId;

        this.description = description;
        this.startedAt = startedAt;
        this.length = length;
        this.amount = amount;
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
        return employerId.equals(contract.employerId) && contractorId.equals(contract.contractorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employerId, contractorId);
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
