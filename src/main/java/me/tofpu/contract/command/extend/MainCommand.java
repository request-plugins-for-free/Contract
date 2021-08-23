package me.tofpu.contract.command.extend;

import co.aikar.commands.annotation.*;
import me.tofpu.contract.util.Util;
import me.tofpu.contract.command.base.ExtraBaseCommand;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.factory.ContractFactory;
import me.tofpu.contract.contract.review.ContractReview;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.service.UserService;
import me.tofpu.contract.util.confirmation.Confirmation;
import me.tofpu.contract.util.confirmation.manager.ConfirmationRegistry;
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
    @CommandCompletion("@players")
    @Syntax("<contractor> <length> <contract-amount> <description>")
    @CommandPermission("contract.create")
    public void onCreate(@Flags("self") final User employer, final User contractor, final long length, final double amount, final String description) {
        if (employer == null || contractor == null) return;

        // if player instance of contractor doesn't exist
        if (!contractor.isPresent()) {
            // TODO: TARGET HAS TO BE ONLINE
            contractor.ifPresent(player -> player.sendMessage("target is not online"));
            return;
        }

        if (isSame(employer, contractor)){
            // TODO: YOU CANNOT SEND YOURSELF A CONTRACT, SILLY
            employer.ifPresent(player -> player.sendMessage("You cannot send yourself a contract, silly!"));
            return;
        }
        // TODO: CHECK IF THE EMPLOYER HAS ENOUGH MONEY

        // if employer has a current contract
        if (employer.currentContract().isPresent()) {
            // TODO: SAY YOU HAVE TO COMPLETE YOUR CURRENT CONTRACT FIRST
            return;
        }
        final Contract contract = ContractFactory.create(employer.name(), employer.uniqueId(), contractor.name(), contractor.uniqueId(), description, length * 60, amount);
        Confirmation.send(employer.uniqueId(), contractor.uniqueId(), contract);

        // TODO: SEND MESSAGE TO EMPLOYER SAYING THE REQUEST HAS BEEN MADE
        employer.ifPresent(player -> player.sendMessage("You have sent a confirmation to " + contractor.name()));
        contractor.ifPresent(player -> player.sendMessage(employer.name() + " has sent you a contract, you can accept/deny the contract by typing /contractor accept/deny (employer)"));

//        employer.ifPresent(player -> player.sendMessage("It's made!"));
    }

    @Subcommand("accept")
    @CommandAlias("accept")
    @CommandCompletion("@players")
    @Syntax("<employer>")
    public void accept(@Flags("self") final User contractor, final User employer, final Confirmation confirmation) {
        // TODO: RELOAD BUG!
        if (contractor == null || employer == null) return;
        if (confirmation == null || isSame(employer, contractor)) {
            // TODO: SEND MESSAGE SAYING YOU DO NOT HAVE A PENDING CONFIRMATION
            contractor.ifPresent(player -> player.sendMessage("You do not have a pending confirmation..."));
            return;
        }
//        if (isSame(employer, contractor)){
//            // TODO: YOU CANNOT ACCEPT YOUR OWN CONTRACT, SILLY
//            employer.ifPresent(player -> player.sendMessage("You cannot accept yourself a contract, silly!"));
//            return;
//        }
        final Contract contract = confirmation.accept();

        employer.currentContract(contract);
        contractor.currentContract(contract);

        contractService.registerContract(contract);

        // TODO: SEND MESSAGE SAYING THE CONTRACT HAS BEEN ACCEPTED
        contractor.ifPresent(player -> player.sendMessage("You have accepted " + employer.name() + " contract request!"));
        employer.ifPresent(player -> player.sendMessage(contractor.name() + " has accepted your contract request!"));
    }

    @Subcommand("deny")
    @CommandAlias("deny")
    @CommandPermission("@players")
    @Syntax("<employer>")
    public void deny(@Flags("self") final User contractor, final User employer, final Confirmation confirmation) {
        // TODO: RELOAD BUG!
        if (contractor == null || employer == null) return;
        if (confirmation == null) {
            // TODO: SEND MESSAGE SAYING YOU DO NOT HAVE A PENDING CONFIRMATION
            contractor.ifPresent(player -> player.sendMessage("You do not have a pending confirmation from " + player.getName()));
            return;
        }
        ConfirmationRegistry.getConfirmationManager().invalidate(confirmation);
        // TODO: SEND MESSAGE SAYING THE CONTRACT HAS BEEN DENIED
        contractor.ifPresent(player -> player.sendMessage("You have denied " + employer.name() + " contract request!"));
        employer.ifPresent(player -> player.sendMessage(contractor.name() + " has denied your contract request!"));
    }

    @Subcommand("rate")
    @CommandAlias("rate")
    @CommandCompletion("@contractsEnded @range:0-5")
    @Syntax("<contract-id> <out-of-five> <review>")
    public void onRate(final Player employer, final String id, final double rate, final String description) {
        final UUID contractId = UUID.fromString(id);
        final Optional<Contract> optional = contractService.getContractById(contractId);
        if (!optional.isPresent()) {
            // TODO: SAY THE CONTRACT DOESN'T EXIST
            return;
        }

        if (rate < 0 || rate > 5) {
            // TODO: SAY THE RATE CANNOT BE LOWER THAN 0 OR HIGHER THAN 5
            return;
        }

        final Contract contract = optional.get();
        if (!contract.hasEnded()) {
            // TODO: SAY THE CONTRACT HASN'T ENDED YET
            return;
        }

        // if contract employer does not equal to the so-called "employer" (issuer) unique id
        if (!contract.employerId().equals(employer.getUniqueId())){
            // TODO: SAY ONLY THE EMPLOYER COULD RATE THIS CONTRACT
            return;
        }

        // TODO: MESSAGE THE CONTRACTOR SAYING THE EMPLOYER HAS RATED YOU
        // TODO: MESSAGE THE EMPLOYER SAYING YOU'VE SUCCESSFULLY RATED THE EMPLOYER

        contract.review().rate((double) Math.round(rate * 100) / 100);
        contract.review().description(description);
    }

    @Subcommand("history")
    @CommandAlias("history")
    @CommandCompletion("@contractsId")
    @Syntax("[contract-id]")
    public void onHistory(final Player player, @co.aikar.commands.annotation.Optional String id) {
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
        if (showAll) {
            final List<Contract> contracts = contractService.of(player.getUniqueId());
            for (final Contract contract : contracts) {
                if (builder.length() != 0) builder.append("\n");
                builder.append(formatContract(contract));
            }
        } else {
            final Optional<Contract> optional = contractService.getContractById(UUID.fromString(id));
            if (!optional.isPresent()) {
                // TODO: SEND MESSAGE SAYING THAT CONTRACT DOESN'T EXIST
                return;
            }
            builder.append(optional.get());
        }

        player.sendMessage(Util.colorize(builder.toString()));
    }

    private boolean isSame(final User one, final User two){
        return one.uniqueId().equals(two.uniqueId());
    }

    private String formatContract(final Contract contract) {
        final String format = " &6&l&m*&r &6Contract &e%contract-id%:\n" + "  &6&l&m*&r &eEmployer: &6%employer-name%\n" + "  &6&l&m*&r &eDescription: &6%description%\n" + "  &6&l&m*&r &eLength: &6%length%\n" + "  &6&l&m*&r &eMoney: &6%money%\n" + " &6&l&m*&r &6Review\n" + "  &6&l&m*&r &eRate: &6%rate% &estars\n" + "  &6&l&m*&r &eReview: &6%review%";
        final ContractReview review = contract.review();

        return Util.WordReplacer.replace(format, new String[]{"%contract-id%", "%employer-name%", "%description%", "%length%", "%money%", "%rate%", "%review%"}, contract.id().toString(), contract.employerName(), contract.description(), contract.length() + "", contract.amount() + "", review.rate() == -1 ? "N/A" : review.rate() + "", review.description() == null ? "N/A" : review.description());
    }
}
