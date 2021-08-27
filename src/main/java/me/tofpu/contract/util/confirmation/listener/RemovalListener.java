package me.tofpu.contract.util.confirmation.listener;

import com.github.benmanes.caffeine.cache.RemovalCause;
import me.tofpu.contract.data.path.Path;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.service.UserService;
import me.tofpu.contract.util.Util;
import me.tofpu.contract.util.confirmation.Confirmation;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.UUID;

public class RemovalListener implements com.github.benmanes.caffeine.cache.RemovalListener<UUID, Confirmation> {
    private final UserService userService;

    public RemovalListener(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onRemoval(@Nullable final UUID key, @Nullable final Confirmation value, @NonNull final RemovalCause cause) {
        if (!cause.wasEvicted()) return;
        final Optional<User> optional = userService.getUser(key);
        optional.ifPresent(user -> Util.message(user, Path.STANDARD_CONTRACT_EXPIRY, new String[]{"%contractor%"}, Bukkit.getOfflinePlayer(value.getReceiver()).getName()));
    }
}
