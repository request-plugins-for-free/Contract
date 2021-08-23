package me.tofpu.contract.contract.runnable;

import me.tofpu.contract.ContractPlugin;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.service.UserService;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ContractRunnable extends BukkitRunnable {
    private static UserService userService;
    private static Economy economy;

    public static void initialize(final UserService userService, final Economy economy){
        ContractRunnable.userService = userService;
        ContractRunnable.economy = economy;
    }
    private final Contract contract;

    public ContractRunnable(final Contract contract) {
        this.contract = contract;
    }

    @Override
    public void run() {
        System.out.println("Has the contract ended?");
        if (this.contract.hasEnded()) {
            System.out.println("It did! cancelling now!");
            cancel();

            //TODO: USE THE PROPER CLASS DEPENDENCY LATER, I'LL FIGURE IT OUT
            Bukkit.getScheduler().runTask(ContractPlugin.getPlugin(ContractPlugin.class), new BukkitRunnable() {
                @Override
                public void run() {
                    final User employer = userService.getUser(contract.employerId()).get();
                    final User contractor = userService.getUser(contract.contractorId()).get();

                    employer.currentContract(null);
                    contractor.currentContract(null);

                    employer.ifPresent(player -> player.sendMessage("Completed!"));
                    contractor.ifPresent(player -> {
                        player.sendMessage("Completed!");
                        economy.depositPlayer(player, contract.amount());
                    });

                    // TODO: SEND MESSAGE SAYING THE CONTRACT HAS COMPLETED
                    // TODO: SEND CONTRACT AMOUNT TO THE CONTRACTOR
                    // TODO: HAVE THE EMPLOYER RATE THE CONTRACTOR? THROUGH A COMMAND MAYBE?
                }
            });
        }
    }

    public void start() {
        System.out.println("Starting timer for " + contract.employerName() + " contract!");
        this.runTaskTimerAsynchronously(ContractPlugin.getPlugin(ContractPlugin.class), 1L, 20L);
    }
}
