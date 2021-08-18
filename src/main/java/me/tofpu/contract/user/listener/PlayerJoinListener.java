package me.tofpu.contract.user.listener;

import me.tofpu.contract.data.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final DataManager dataManager;

    public PlayerJoinListener(final DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerJoin(final PlayerJoinEvent event){
        this.dataManager.loadUser(event.getPlayer().getUniqueId());
    }
}
