package me.tofpu.contract.listener;

import me.tofpu.contract.user.service.UserService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChat implements Listener {
    private final UserService userService;

    public AsyncPlayerChat(final UserService userService) {
        this.userService = userService;
    }

    @EventHandler(ignoreCancelled = true)
    private void onAsyncPlayerChat(final AsyncPlayerChatEvent event) {
        final String format = "&6&m&l⩧&r&e%rate% &6%1$s⋟ &e%2$s";
        event.setFormat(format.replace("%rate%", userService.getUser(event.getPlayer().getUniqueId()).get().averageRating() + ""));
    }
}
