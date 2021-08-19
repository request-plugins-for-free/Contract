package me.tofpu.contract.user.listener;

import me.tofpu.contract.data.DataManager;
import me.tofpu.contract.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

public class PlayerJoinListener implements Listener {
    private final DataManager dataManager;

    public PlayerJoinListener(final DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerJoin(final PlayerJoinEvent event){
        final Player player = event.getPlayer();
        final Optional<User> user = this.dataManager.loadUser(player);

        user.ifPresent(value -> value.name(player.getName()));
    }
}
