package me.tofpu.contract.util.confirmation.manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import me.tofpu.contract.util.confirmation.Confirmation;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ConfirmationRegistry {
    private static final ConfirmationRegistry CONFIRMATION_MANAGER = new ConfirmationRegistry();
    public static ConfirmationRegistry getConfirmationManager() {
        return CONFIRMATION_MANAGER;
    }
    private final Cache<UUID, Confirmation> confirmations;

    public ConfirmationRegistry() {
        // TODO: HAVE THE EXPIRY THING CONFIGURABLE
        this.confirmations = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build();
    }

    public Confirmation register(final Confirmation confirmation){
        this.confirmations.put(confirmation.getSender(), confirmation);
        return confirmation;
    }

    public Optional<Confirmation> get(final UUID uuid, boolean sender){
        System.out.println(sender + " | " + uuid.toString());
        if (sender)
            return Optional.ofNullable(this.confirmations.getIfPresent(uuid));
        return get(uuid);
    }

    private Optional<Confirmation> get(final UUID receiver){
        for (final Confirmation confirmation : this.confirmations.asMap().values()){
            if (confirmation.getReceiver().equals(receiver)){
                System.out.println(confirmation.toString());
                return Optional.of(confirmation);
            }
        }
        return Optional.empty();
    }

    public void invalidate(final Confirmation confirmation){
        this.confirmations.invalidate(confirmation.getSender());
    }

    public void invalidate(final UUID sender){
        this.confirmations.invalidate(sender);
    }

    public void invalidateAll(){
        this.confirmations.invalidateAll();
    }
}
