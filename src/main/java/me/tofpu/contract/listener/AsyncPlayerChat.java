package me.tofpu.contract.listener;

import me.tofpu.contract.data.path.Path;
import me.tofpu.contract.user.User;
import me.tofpu.contract.user.service.UserService;
import me.tofpu.contract.util.Util;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

public class AsyncPlayerChat implements Listener {
    private final UserService userService;

    public AsyncPlayerChat(final UserService userService) {
        this.userService = userService;
    }

    @EventHandler(ignoreCancelled = true)
    private void onAsyncPlayerChat(final AsyncPlayerChatEvent event) {
        if (!Path.SETTINGS_CHAT_DISABLE.getValue()) return;
        
        final Optional<User> optional = userService.getUser(event.getPlayer().getUniqueId());
        optional.ifPresent(user -> Util.message(user, Path.SETTINGS_CHAT_FORMAT, new String[]{"%name%", "%message%"}, "%1$s", "%2$s"));
    }
}
