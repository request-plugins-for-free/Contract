package me.tofpu.contract.util.confirmation.manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.requestpluginsforfree.config.ConfigAPI;
import com.github.requestpluginsforfree.config.type.config.ConfigType;
import me.tofpu.contract.user.service.UserService;
import me.tofpu.contract.util.confirmation.Confirmation;
import me.tofpu.contract.util.confirmation.listener.RemovalListener;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ConfirmationRegistry {
    private static final ConfirmationRegistry CONFIRMATION_MANAGER = new ConfirmationRegistry();
    public static ConfirmationRegistry getConfirmationManager() {
        return CONFIRMATION_MANAGER;
    }
    private final Cache<UUID, Confirmation> confirmations;

    public ConfirmationRegistry(final UserService userService) {
        final Integer expiry = ConfigAPI.get("config","settings.expire-on", ConfigType.INTEGER);

        this.confirmations = Caffeine.newBuilder()
                .expireAfterWrite(expiry == null ? 1 : expiry, TimeUnit.MINUTES)
                .evictionListener(new RemovalListener(userService))
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
                System.out.println(confirmation);
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
