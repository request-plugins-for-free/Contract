package me.tofpu.contract;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class Util {
    public static Optional<UUID> getUniqueId(final String name){
        final Player player = Bukkit.getPlayerExact(name);
        return Optional.ofNullable(player == null ? null : player.getUniqueId());
    }
}
