package me.tofpu.contract.util.confirmation;

import me.tofpu.contract.contract.Contract;
import me.tofpu.contract.contract.factory.ContractFactory;
import me.tofpu.contract.util.confirmation.manager.ConfirmationRegistry;

import java.util.UUID;

public class Confirmation {
    public static Confirmation send(final UUID sender, final UUID target, final Contract contract){
        return ConfirmationRegistry.getConfirmationManager().register(new Confirmation(sender, target, contract));
    }
    private final UUID sender;
    private final UUID receiver;
    private final Contract contract;

    public Confirmation(final UUID sender, final UUID receiver, final Contract contract) {
        this.sender = sender;
        this.receiver = receiver;
        this.contract = contract;
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public Contract peek(){
        return ContractFactory.clone(contract);
    }

    public Contract accept(){
        ConfirmationRegistry.getConfirmationManager().invalidate(this);
        contract.freeze(false);
        return contract;
    }

    public void invalidate(){
        ConfirmationRegistry.getConfirmationManager().invalidate(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Confirmation{");
        sb.append("sender=").append(sender);
        sb.append(", receiver=").append(receiver);
        sb.append('}');
        return sb.toString();
    }
}
