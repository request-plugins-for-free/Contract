package me.tofpu.contract.contract.impl;

import me.tofpu.contract.ContractPlugin;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.review.ContractReview;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ContractImpl implements Contract {
    private final UUID id;

    private String employerName;
    private final UUID employerId;

    private String contractorName;
    private final UUID contractorId;

    private final ContractReview review;

    private final long startedAt;
    private final long length;
    private final double amount;
    private final String description;

    public ContractImpl(final UUID id, final String employerName, final UUID employerId, final String contractorName, final UUID contractorId, final ContractReview review, final String description, final long startedAt, final long length, final double amount) {
        this.id = id;
        this.employerName = employerName;
        this.employerId = employerId;

        this.contractorName = contractorName;
        this.contractorId = contractorId;

        this.review = review;

        this.description = description;
        this.startedAt = startedAt;
        this.length = length;
        this.amount = amount;

        // This is for ending the contract once it has reached it's supposedly length
        new BukkitRunnable() {
            @Override
            public void run() {
                if (hasEnded()) {
                    cancel();

                    //TODO: USE THE PROPER CLASS DEPENDENCY LATER, I'LL FIGURE IT OUT
                    Bukkit.getScheduler().runTask(ContractPlugin.getPlugin(ContractPlugin.class), new BukkitRunnable() {
                        @Override
                        public void run() {
                            final Player employer = Bukkit.getPlayer(employerId());
                            final Player contractor = Bukkit.getPlayer(contractorId());

                            employer.sendMessage("Completed!");
                            contractor.sendMessage("Completed!");
                            // TODO: SEND MESSAGE SAYING THE CONTRACT HAS COMPLETED
                            // TODO: SEND CONTRACT AMOUNT TO THE CONTRACTOR
                            // TODO: HAVE THE EMPLOYER RATE THE CONTRACTOR? THROUGH A COMMAND MAYBE?
                        }
                    });
                }
            }
        }.runTaskTimerAsynchronously(ContractPlugin.getPlugin(ContractPlugin.class), 0, 20);
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
        return duration.toMinutes() >= length();
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
        sb.append(", employerName='").append(employerName).append('\'');
        sb.append(", employerId=").append(employerId);
        sb.append(", contractorName='").append(contractorName).append('\'');
        sb.append(", contractorId=").append(contractorId);
        sb.append(", review=").append(review);
        sb.append(", startedAt=").append(startedAt);
        sb.append(", length=").append(length);
        sb.append(", amount=").append(amount);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
