package me.tofpu.contract.user.impl;

import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.service.UserService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class UserImpl implements User {
    private static ContractService contractService;
    public static void initialize(final ContractService contractService) {
        UserImpl.contractService = contractService;
    }

    private final Player player;
    private final UUID uniqueId;
    private String name;
    private Contract currentContract;
    private double totalRating;

    public UserImpl(final UUID uniqueId) {
        this.player = Bukkit.getPlayer(uniqueId);
        this.uniqueId = uniqueId;
    }

    public UserImpl(final String name, final UUID uniqueId, final Contract currentContract, final double totalRating) {
        this(uniqueId);
        this.name = name;
        this.currentContract = currentContract;
        this.totalRating = totalRating;
    }

    /**
     * @return the user name
     */
    @Override
    public String name() {
        return name;
    }

    /**
     * @param newName the player name
     */
    @Override
    public void name(final String newName) {
        this.name = newName;
    }

    /**
     * @return the user unique id
     */
    @Override
    public UUID uniqueId() {
        return uniqueId;
    }

    /**
     * @return the user current contract, if it's available
     */
    @Override
    public Optional<Contract> currentContract() {
        return Optional.ofNullable(currentContract);
    }

    /**
     * @param currentContract the current contract the user is in
     */
    @Override
    public void currentContract(final Contract currentContract) {
        this.currentContract = currentContract;
    }

    /**
     * @return returns true if player instance exists otherwise false
     */
    @Override
    public boolean isPresent(){
        return this.player != null;
    }

    @Override
    public void ifPresent(final Consumer<Player> consumer) {
        if (this.player != null) consumer.accept(player);
    }

    /**
     * @return the user total rating
     */
    @Override
    public double totalRating() {
        // FIXME: 8/20/2021 
        return totalRating;
    }

    /**
     * @return the user average rating
     */
    @Override
    public double averageRating() {
        double average = 0;
        final List<Contract> contracts = contractService.of(uniqueId());
        for (final Contract contract : contracts){
            average+= contract.review().rate();
        }
        return average / contracts.size();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof UserImpl)) return false;
        final UserImpl user = (UserImpl) o;
        return uniqueId().equals(user.uniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueId());
    }
}
