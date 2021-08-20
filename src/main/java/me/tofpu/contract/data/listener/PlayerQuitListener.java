package me.tofpu.contract.data.listener;

import me.tofpu.contract.data.DataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final DataManager dataManager;

    public PlayerQuitListener(final DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerQuit(final PlayerQuitEvent event) {
        dataManager.saveUser(event.getPlayer());
    }
}
