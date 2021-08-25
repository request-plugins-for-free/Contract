package me.tofpu.contract.util;

import co.aikar.commands.RegisteredCommand;
import com.github.requestpluginsforfree.dependency.api.DependencyAPI;
import com.google.common.collect.Maps;
import me.clip.placeholderapi.PlaceholderAPI;
import me.tofpu.contract.data.path.Path;
import me.tofpu.contract.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Util {
    public static Optional<UUID> getUniqueId(final String name) {
        final Player player = Bukkit.getPlayerExact(name);
        return Optional.ofNullable(player == null ? null : player.getUniqueId());
    }

    public static String colorize(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void message(final User user, final Path.Value<?> value){
        user.ifPresent(player -> message(player, value));
    }

    public static void message(final User user, final Path.Value<?> value, final String[] replaceArray, final String... replaceWith){
        user.ifPresent(player -> message(player, value, replaceArray, replaceWith));
    }

    public static void message(final CommandSender sender, final Path.Value<?> value){
        message(sender, value.getValue() + "");
    }

    public static void message(final CommandSender sender, String message){
        if (DependencyAPI.get("dependency").isAvailable() && sender instanceof Player) message = PlaceholderAPI.setBracketPlaceholders(((Player) sender).getPlayer(), message);
        sender.sendMessage(colorize(message));
    }

    public static void message(final CommandSender sender, final Path.Value<?> value, final String[] replaceArray, final String... replaceWith){
        message(sender, colorize(WordReplacer.replace(value.getValue() + "", replaceArray, replaceWith)));
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
