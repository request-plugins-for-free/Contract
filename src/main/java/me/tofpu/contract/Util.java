package me.tofpu.contract;

import co.aikar.commands.RegisteredCommand;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Util {
    public static Optional<UUID> getUniqueId(final String name){
        final Player player = Bukkit.getPlayerExact(name);
        return Optional.ofNullable(player == null ? null : player.getUniqueId());
    }

    public static String colorize(final String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String format(final RegisteredCommand<?> command) {
        final String format = " &6&l&m*&r &e/%command% &6%syntax% &6&l&m-&r &e%description%";

        final Map<String, String> map = Maps.newHashMap();
        map.put("%command%", command.getCommand());
        map.put("%syntax%", command.getSyntaxText());
        map.put("%description%", command.getHelpText());
        return Util.colorize(Util.WordReplacer.replace(format, map));
    }

    public static class WordReplacer {
        public static String replace(String message, final String[] replaceArray, final String... replaceWith) {
            if (replaceArray == null) return message;
            for (int i = 0; i < replaceArray.length; i++) {
                message = message.replace(replaceArray[i], replaceWith[i]);
            }
            return message;
        }

        public static String replace(String message, final Map<String, ?> replaceMap) {
            if (replaceMap == null) return message;
            for (final Map.Entry<String, ?> replace : replaceMap.entrySet()) {
                message = message.replace(replace.getKey(), replace.getValue() + "");
            }
            return message;
        }
    }
}
