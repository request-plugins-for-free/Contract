package me.tofpu.contract.command.extend;

import co.aikar.commands.annotation.*;
import me.tofpu.contract.command.base.ExtraBaseCommand;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.factory.ContractFactory;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.properties.stars.review.UserReview;
import me.tofpu.contract.user.properties.stars.review.factory.UserReviewFactory;
import me.tofpu.contract.user.service.UserService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

@CommandAlias("contract")
public class MainCommand extends ExtraBaseCommand {
    private final UserService userService;
    private final ContractService contractService;

    public MainCommand(final UserService userService, final ContractService contractService) {
        super("contract");
        this.userService = userService;
        this.contractService = contractService;
    }

    @Subcommand("create")
    @CommandAlias("create")
    @Syntax("<player> <contract-time-in-minutes> <contract-amount> <description>")
    @CommandPermission("contract.create")
    public void onCreate(final Player employer, @Split String[] args){
        final Player contractor = Bukkit.getPlayer(args[0]);
        if (contractor == null || !contractor.isOnline()){
            // TODO: TARGET HAS TO BE ONLINE
            return;
        }
        final long length = Long.parseLong(args[1]);
        final double amount = Double.parseDouble(args[2]);
        final String description = args[3];

        // TODO: CHECK IF THE EMPLOYER HAS ENOUGH MONEY

        final Optional<User> optionalEmployer = userService.getUser(employer.getUniqueId());
        if (!optionalEmployer.isPresent()) return;
        final Optional<User> optionalContractor = userService.getUser(contractor.getUniqueId());
        if (!optionalContractor.isPresent()) return;

        final Contract contract = ContractFactory.create(
                employer.getName(), employer.getUniqueId(),
                contractor.getName(), contractor.getUniqueId(),
                description, length, amount);

        optionalContractor.get().currentContract(contract);
        // TODO: SEND MESSAGE SAYING THE CONTRACT HAS BEEN MADE!
    }

    @CommandAlias("rate")
    @Syntax("<contract-id> <out-of-five> <review>")
    public void onRate(final Player employer, @Split String[] args){
        final UUID contractId = UUID.fromString(args[0]);
        final Optional<Contract> optional = contractService.getContractById(contractId);
        if (!optional.isPresent()){
            // TODO: SAY THE CONTRACT DOESN'T EXIST
            return;
        }

        final Contract contract = optional.get();
        if (!contract.hasEnded()){
            // TODO: SAY THE CONTRACT HASN'T ENDED YET
            return;
        }

        final User user = userService.getUser(contract.contractorId()).get();
        final double rating = Double.parseDouble(args[1]);
        StringBuilder description = new StringBuilder();
        for (int i = 2; i < args.length; i++){
            description.append(args[i]);
        }

        final UserReview review = UserReviewFactory.create(user.name(), user.uniqueId(), rating, description.toString());
        // TODO: MESSAGE THE CONTRACTOR SAYING THE EMPLOYER HAS RATED YOU
        // TODO: MESSAGE THE EMPLOYER SAYING YOU'VE SUCCESSFULLY RATED THE EMPLOYER
        user.ratedBy().add(review);
    }
}
