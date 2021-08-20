package me.tofpu.contract.command.extend;

import co.aikar.commands.annotation.*;
import me.tofpu.contract.Util;
import me.tofpu.contract.command.base.ExtraBaseCommand;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.factory.ContractFactory;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.user.User;
import me.tofpu.contract.contract.review.ContractReview;
import me.tofpu.contract.contract.review.factory.ContractReviewFactory;
import me.tofpu.contract.user.service.UserService;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
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

    @Override
    @Private
    @HelpCommand
    public void onHelp(final CommandSender sender) {
        super.onHelp(sender);
    }

    @Override
    @Private
    @CatchUnknown
    public void onUnknownCommand(final CommandSender sender) {
        super.onUnknownCommand(sender);
    }

    @Subcommand("create")
    @CommandAlias("create")
    @CommandCompletion("@players @range:1-500 @range:1-2000")
    @Syntax("<player> <contract-time-in-minutes> <contract-amount> <description>")
    @CommandPermission("contract.create")
    public void onCreate(final Player employer, final String playerName, final long length, final double amount, final String description){
        final Player contractor = Bukkit.getPlayerExact(playerName);
        if (contractor == null || !contractor.isOnline()){
            // TODO: TARGET HAS TO BE ONLINE
            employer.sendMessage("target is not online");
            return;
        }
        // TODO: CHECK IF THE EMPLOYER HAS ENOUGH MONEY

        final Optional<User> optionalEmployer = userService.getUser(employer.getUniqueId());
        if (!optionalEmployer.isPresent()){
            employer.sendMessage("you do not have a user registered");
            return;
        }
        final Optional<User> optionalContractor = userService.getUser(contractor.getUniqueId());
        if (!optionalContractor.isPresent()){
            employer.sendMessage("the target does not have a user registered");
            return;
        }

        final Contract contract = ContractFactory.create(
                employer.getName(), employer.getUniqueId(),
                contractor.getName(), contractor.getUniqueId(),
                description, length, amount);

        optionalEmployer.get().currentContract(contract);
        optionalContractor.get().currentContract(contract);

        contractService.registerContract(contract);
        // TODO: SEND MESSAGE SAYING THE CONTRACT HAS BEEN MADE!
        employer.sendMessage("It's made!");
    }

    @Subcommand("rate")
    @CommandAlias("rate")
    @CommandCompletion("@selfContractsId @range:0-5")
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

        final double rating = Double.parseDouble(args[1]);
        StringBuilder description = new StringBuilder();
        for (int i = 2; i < args.length; i++){
            description.append(args[i]);
        }

        // TODO: MESSAGE THE CONTRACTOR SAYING THE EMPLOYER HAS RATED YOU
        // TODO: MESSAGE THE EMPLOYER SAYING YOU'VE SUCCESSFULLY RATED THE EMPLOYER

        contract.review().rate(rating);
        contract.review().description(description.toString());
    }

    @Subcommand("history")
    @CommandAlias("history")
    @Syntax("[contract-id]")
    public void onHistory(final Player player, @co.aikar.commands.annotation.Optional String id){
        final boolean showAll = id == null || id.isEmpty();
        // > Contract JAI1JDJA-1J29-GHMAHF
        // Employer: HoodBoy
        // Description: Clean up my Minecraft House
        // Length: 20 minutes
        // Money: 1000$
        // > Review
        // Rate: 1 stars
        // Review: this motherfucker' somehow made it worse & stole my fuckin' money, I ain't hirin' nobody no more.

        final StringBuilder builder = new StringBuilder();
        if (showAll){
            final List<Contract> contracts = contractService.of(player.getUniqueId());
            for (final Contract contract : contracts){
                if (builder.length() != 0) builder.append("\n");
                builder.append(formatContract(contract));
            }
        } else {
            final Optional<Contract> optional = contractService.getContractById(UUID.fromString(id));
            if (!optional.isPresent()){
                // TODO: SEND MESSAGE SAYING THAT CONTRACT DOESN'T EXIST
                return;
            }
            builder.append(optional.get());
        }

        player.sendMessage(Util.colorize(builder.toString()));
    }

    private String formatContract(final Contract contract){
        final String format =
                " &6&l&m*&r &6Contract &e%contract-id%:\n" +
                        "  &6&l&m*&r &eEmployer: &6%employer-name%\n" +
                        "  &6&l&m*&r &eDescription: &6%description%\n" +
                        "  &6&l&m*&r &eLength: &6%length%\n" +
                        "  &6&l&m*&r &eMoney: &6%money%\n" +
                        " &6&l&m*&r &6Review\n" +
                        "  &6&l&m*&r &eRate: &6%rate% &estars\n" +
                        "  &6&l&m*&r &eReview: &6%review%";
        final ContractReview review = contract.review();

        return Util.WordReplacer.replace(format,
                new String[]{
                        "%contract-id%",
                        "%employer-name%",
                        "%description%",
                        "%length%",
                        "%money%",
                        "%rate%",
                        "%review%"},
                contract.id().toString(),
                contract.employerName(),
                contract.description(),
                contract.length() + "",
                contract.amount() + "",
                review.rate() == -1 ? "N/A" : review.rate() + "",
                review.description() == null ? "N/A" : review.description());
    }
}
