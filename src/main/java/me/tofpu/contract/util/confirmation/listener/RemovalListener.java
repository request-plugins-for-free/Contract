package me.tofpu.contract.util.confirmation.listener;

import me.tofpu.contract.data.path.Path;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.service.UserService;
import me.tofpu.contract.util.Util;
import me.tofpu.contract.util.confirmation.Confirmation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class RemovalListener extends BukkitRunnable {
    private final UserService userService;
    private final Map<UUID, Confirmation> confirmationMap;

    public RemovalListener(final Plugin plugin, final UserService userService, final Map<UUID, Confirmation> confirmationMap) {
        this.userService = userService;
        this.confirmationMap = confirmationMap;
        runTaskTimerAsynchronously(plugin, 1L, (20L * 10L));
    }

    public void onRemoval(final UUID key, final Confirmation value) {
        final Optional<User> optional = userService.getUser(key);
        optional.ifPresent(user -> Util.message(user, Path.STANDARD_CONTRACT_EXPIRY, new String[]{"%contractor%"}, Bukkit.getOfflinePlayer(value.getReceiver()).getName()));
    }

    @Override
    public void run() {
        final Iterator<Confirmation> iterator = confirmationMap.values().iterator();
        while (iterator.hasNext()){
            final Confirmation confirmation = iterator.next();
            final Duration duration = Duration.ofNanos(System.nanoTime() - confirmation.getCreation());
            if (duration.toMinutes() >= Path.SETTINGS_EXPIRE_ON.getValue()){
                onRemoval(confirmation.getSender(), confirmation);
                confirmation.invalidate();
                return;
            }
        }
    }
}
