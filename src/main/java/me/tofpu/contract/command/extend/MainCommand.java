package me.tofpu.contract.command.extend;

import co.aikar.commands.annotation.*;
import me.tofpu.contract.data.path.Path;
import me.tofpu.contract.user.service.UserService;
import me.tofpu.contract.util.Util;
import me.tofpu.contract.command.base.ExtraBaseCommand;
import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.factory.ContractFactory;
import me.tofpu.contract.contract.review.ContractReview;
import me.tofpu.contract.contract.service.ContractService;
import me.tofpu.contract.user.User;
import me.tofpu.contract.util.confirmation.Confirmation;
import me.tofpu.contract.util.confirmation.manager.ConfirmationRegistry;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@CommandAlias("contract")
public class MainCommand extends ExtraBaseCommand {
    private final UserService userService;
    private final ContractService contractService;
    private final Economy economy;

    public MainCommand(final UserService userService, final Economy economy, final ContractService contractService) {
        super("contract");
        this.userService = userService;
        this.economy = economy;
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
//            contractor.ifPresent(player -> player.sendMessage("target is not online"));
            Util.message(employer, Path.ERROR_TARGET_OFFLINE, new String[]{"%name%"}, contractor.name());
            return;
        }

        if (isSame(employer, contractor)){
//            employer.ifPresent(player -> player.sendMessage("You cannot send yourself a contract, silly!"));
            Util.message(employer, Path.ERROR_CONTRACT_SELF);
            return;
        }

        // if employer has a current contract
        if (employer.currentContract().isPresent()) {
            Util.message(employer, Path.ERROR_CONTRACT_BUSY);
            return;
        }

        final AtomicBoolean enough = new AtomicBoolean(false);
        employer.ifPresent(player ->
                enough.set(this.economy.getBalance(player) >= amount)
        );

        // if employer doesn't have enough money
        if (!enough.get()){
//            employer.ifPresent(player -> player.sendMessage("You do not have enough money!"));
            Util.message(employer, Path.ERROR_CONTRACT_NOT_ENOUGH_FUNDS);
            return;
        }

        final Contract contract = ContractFactory.create(employer.name(), employer.uniqueId(), contractor.name(), contractor.uniqueId(), description, length * 60, amount);
        Confirmation.send(employer.uniqueId(), contractor.uniqueId(), contract);

//        employer.ifPresent(player -> player.sendMessage("You have sent a confirmation to " + contractor.name()));
//        contractor.ifPresent(player -> player.sendMessage(employer.name() + " has sent you a contract, you can accept/deny the contract by typing /contractor accept/deny (employer)"));

        Util.message(employer, Path.STANDARD_CONTRACT_SENT_TO, new String[]{"%name%"}, contractor.name());
        Util.message(contractor, Path.STANDARD_CONTRACT_SENT_FROM, new String[]{"%name%"}, employer.name());
    }

    @Subcommand("accept")
    @CommandAlias("accept")
    @CommandCompletion("@players")
    @Syntax("<employer>")
    public void accept(@Flags("self") final User contractor, final User employer, final Confirmation confirmation) {
        // TODO: RELOAD BUG!
        if (contractor == null || employer == null) return;
        if (confirmation == null || isSame(employer, contractor)) {
//            contractor.ifPresent(player -> player.sendMessage("You do not have a pending confirmation..."));
            Util.message(contractor, Path.ERROR_REQUEST_NO_PENDING);
            return;
        }
        // if employer doesn't have enough amount
        if (!hasEnough(employer, confirmation.peek().amount())){
            confirmation.invalidate();
//            employer.ifPresent(player -> player.sendMessage("Your contract has been cancelled due to lack of enough funds."));
//            contractor.ifPresent(player -> player.sendMessage("The contract has been cancelled due to lack of employer's funds."));

            Util.message(employer, Path.ERROR_REQUEST_LACKING_FUNDS_FROM, new String[]{"%name%"}, contractor.name());
            Util.message(contractor, Path.ERROR_REQUEST_LACKING_FUNDS_TO, new String[]{"%name%"}, employer.name());
            return;
        }
        final Contract contract = confirmation.accept();
        employer.ifPresent(player -> this.economy.withdrawPlayer(player, contract.amount()));

        employer.currentContract(contract);
        contractor.currentContract(contract);

        contractService.registerContract(contract);

//        contractor.ifPresent(player -> player.sendMessage("You have accepted " + employer.name() + " contract request!"));
        Util.message(contractor, Path.STANDARD_REQUEST_ACCEPTED_TO, new String[]{"%name%"}, employer.name());

//        employer.ifPresent(player -> player.sendMessage(contractor.name() + " has accepted your contract request!"));
        Util.message(employer, Path.STANDARD_REQUEST_ACCEPTED_FROM, new String[]{"%name%"}, contractor.name());
    }

    @Subcommand("deny")
    @CommandAlias("deny")
    @CommandPermission("@players")
    @Syntax("<employer>")
    public void deny(@Flags("self") final User contractor, final User employer, final Confirmation confirmation) {
        // TODO: RELOAD BUG!
        if (contractor == null || employer == null) return;
        if (confirmation == null) {
//            contractor.ifPresent(player -> player.sendMessage("You do not have a pending confirmation from " + player.getName()));
            Util.message(contractor, Path.ERROR_REQUEST_NO_PENDING);
            return;
        }
        ConfirmationRegistry.getConfirmationRegistry().invalidate(confirmation);

        // contractor.ifPresent(player -> player.sendMessage("You have denied " + employer.name() + " contract request!"));
        Util.message(contractor, Path.STANDARD_REQUEST_DENIED_TO, new String[]{"%name%"}, employer.name());

//        employer.ifPresent(player -> player.sendMessage(contractor.name() + " has denied your contract request!"));
        Util.message(employer, Path.STANDARD_REQUEST_DENIED_FROM, new String[]{"%name%"}, contractor.name());
    }

    @Subcommand("current")
    @CommandAlias("current")
    public void onCurrent(final User user, final Contract contract){
        user.ifPresent(player -> Util.message(player, formatContract(contract)));
    }

    @Subcommand("rate")
    @CommandAlias("rate")
    @CommandCompletion("@contractsEnded @range:0-5")
    @Syntax("<contract-id> <out-of-five> <review>")
    public void onRate(final Player employer, final String id, final double rate, final String description) {
        final UUID contractId = UUID.fromString(id);
        final Optional<Contract> optional = contractService.getContractById(contractId);
        if (!optional.isPresent()) {
            Util.message(employer, Path.ERROR_GENERAL_INVALID_CONTRACT);
            return;
        }

        if (rate < 0 || rate > 5) {
            Util.message(employer, Path.ERROR_RATE_INVALID_RATE);
            return;
        }

        final Contract contract = optional.get();
        if (!contract.hasEnded()) {
            Util.message(employer, Path.ERROR_RATE_ACTIVE_CONTRACT);
            return;
        }

        // if contract employer does not equal to the so-called "employer" (issuer) unique id
        if (!contract.employerId().equals(employer.getUniqueId())){
            Util.message(employer, Path.ERROR_RATE_ONLY_EMPLOYER);
            return;
        }

        // TODO: MESSAGE THE CONTRACTOR SAYING THE EMPLOYER HAS RATED YOU
        // TODO: MESSAGE THE EMPLOYER SAYING YOU'VE SUCCESSFULLY RATED THE EMPLOYER

        contract.review().rate((double) Math.round(rate * 100) / 100);
        contract.review().description(description);

        final User contractor = userService.getUser(contract.contractorId()).get();

        Util.message(employer, Path.STANDARD_RATE_RATED_TO, new String[]{"%name%"}, contractor.name());
        Util.message(contractor, Path.STANDARD_RATE_RATED_FROM, new String[]{"%name%", "%id%"}, employer.getDisplayName(), contractId.toString());
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
                if (builder.capacity() != 1) builder.append("\n");
                builder.append(formatContract(contract));
            }
        } else {
            final Optional<Contract> optional = contractService.getContractById(UUID.fromString(id));
            if (!optional.isPresent()) {
                Util.message(player, Path.ERROR_GENERAL_INVALID_CONTRACT);
                return;
            }
            builder.append(formatContract(optional.get()));
        }

        player.sendMessage(Util.colorize(builder.toString()));
    }

    private boolean isSame(final User one, final User two){
        return one.uniqueId().equals(two.uniqueId());
    }

    private boolean hasEnough(final User employer, final double amount){
        final boolean[] enough = {false};
        employer.ifPresent(player -> enough[0] = this.economy.getBalance(player) >= amount);

        return enough[0];
    }

    private String formatContract(final Contract contract) {
        final String format = " &6&l&m*&r &6Contract &e%contract-id%:\n" +
                "  &6&l&m*&r &eEmployer: &6%employer-name%\n" +
                "  &6&l&m*&r &eContractor: &6%contractor-name%\n" +
                "  &6&l&m*&r &eStatus: &6%status%\n" +
                "  &6&l&m*&r &eDescription: &6%description%\n" +
                "  &6&l&m*&r &eLength: &6%length%\n" +
                "  &6&l&m*&r &eMoney: &6%money%\n" +
                " &6&l&m*&r &6Review\n" +
                "  &6&l&m*&r &eRate: &6%rate% &estars\n" +
                "  &6&l&m*&r &eReview: &6%review%";
        final ContractReview review = contract.review();

        return Util.WordReplacer.replace(format, new String[]{"%contract-id%", "%employer-name%", "%contractor-name%", "%status%", "%description%", "%length%", "%money%", "%rate%", "%review%"}, contract.id().toString(), contract.employerName(), contract.employerName(), contract.hasEnded() ? "Ended" : "Available", contract.description(), contract.length() + "", contract.amount() + "", review.rate() == -1 ? "N/A" : review.rate() + "", review.description() == null ? "N/A" : review.description());
    }
}
