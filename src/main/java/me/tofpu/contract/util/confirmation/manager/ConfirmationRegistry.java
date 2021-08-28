package me.tofpu.contract.util.confirmation.manager;

import me.tofpu.contract.user.service.UserService;
import me.tofpu.contract.util.confirmation.Confirmation;
import me.tofpu.contract.util.confirmation.listener.RemovalListener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ConfirmationRegistry {
    private static ConfirmationRegistry confirmationRegistry;

    public static ConfirmationRegistry getConfirmationRegistry() {
        return confirmationRegistry;
    }

    public static void initialize(final Plugin plugin, final UserService userService) {
        ConfirmationRegistry.confirmationRegistry = new ConfirmationRegistry(plugin, userService);
    }

    private final Map<UUID, Confirmation> confirmations;

    public ConfirmationRegistry(final Plugin plugin, final UserService userService) {
        this.confirmations = new HashMap<>();
        new RemovalListener(plugin, userService, confirmations);
    }

    public Confirmation register(final Confirmation confirmation) {
        this.confirmations.put(confirmation.getSender(), confirmation);
        return confirmation;
    }

    public Optional<Confirmation> get(final UUID uuid, boolean sender) {
        if (sender) return Optional.ofNullable(this.confirmations.get(uuid));
        return get(uuid);
    }

    private Optional<Confirmation> get(final UUID receiver) {
        for (final Confirmation confirmation : this.confirmations.values()) {
            if (confirmation.getReceiver().equals(receiver)) {
                return Optional.of(confirmation);
            }
        }
        return Optional.empty();
    }

    public void invalidate(final Confirmation confirmation) {
        this.confirmations.remove(confirmation.getSender());
    }

    public void invalidate(final UUID sender) {
        this.confirmations.remove(sender);
    }

    public void invalidateAll() {
        this.confirmations.clear();
    }
}
