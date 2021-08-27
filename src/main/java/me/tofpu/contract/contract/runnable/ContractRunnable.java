package me.tofpu.contract.contract.runnable;

import me.tofpu.contract.ContractPlugin;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.data.path.Path;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.service.UserService;
import me.tofpu.contract.util.Util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

                    final Player employerPlayer = employer.player();
                    final Player contractorPlayer = contractor.player();

                    Util.message(employerPlayer, Path.STANDARD_CONTRACT_COMPLETED_FROM, new String[]{"%id%"}, contract.id().toString());

                    Util.message(contractorPlayer, Path.STANDARD_CONTRACT_COMPLETED_TO, new String[]{"%amount%"}, contract.amount() + "");
                    economy.depositPlayer(contractorPlayer, contract.amount());
                }
            });
        }
    }

    public void start() {
        System.out.println("Starting timer for " + contract.employerName() + " contract!");
        this.runTaskTimerAsynchronously(ContractPlugin.getPlugin(ContractPlugin.class), 1L, 20L);
    }
}
