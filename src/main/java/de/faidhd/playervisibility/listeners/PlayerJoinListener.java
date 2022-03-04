package de.faidhd.playervisibility.listeners;

import de.faidhd.playervisibility.PlayerVisibilityPlugin;
import de.faidhd.playervisibility.objects.VisiblePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final PlayerVisibilityPlugin plugin;

    public PlayerJoinListener(PlayerVisibilityPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        VisiblePlayer visiblePlayer = plugin.getDataManager().getVisiblePlayer(event.getPlayer().getUniqueId());
        if (!plugin.getDataManager().getVisiblePlayers().contains(visiblePlayer))
            plugin.getDataManager().getVisiblePlayers().add(visiblePlayer);
        plugin.getDataManager().updateVisibility(player, visiblePlayer);
        for (VisiblePlayer vPlayers : plugin.getDataManager().getVisiblePlayers()) {
            Player target = plugin.getServer().getPlayer(vPlayers.getUuid());
            if (target == null) continue;
            plugin.getDataManager().updateVisibility(target, vPlayers);
        }
    }
}
