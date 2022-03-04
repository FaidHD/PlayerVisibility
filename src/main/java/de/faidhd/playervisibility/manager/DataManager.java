package de.faidhd.playervisibility.manager;

import de.faidhd.playervisibility.PlayerVisibilityPlugin;
import de.faidhd.playervisibility.objects.VisibilityType;
import de.faidhd.playervisibility.objects.VisiblePlayer;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class DataManager {

    private final PlayerVisibilityPlugin plugin;

    @Getter
    private ArrayList<VisiblePlayer> visiblePlayers;
    @Getter
    private Location spawnLocation;

    public DataManager(PlayerVisibilityPlugin plugin) {
        this.plugin = plugin;
        this.visiblePlayers = new ArrayList<>();
        if (plugin.getConfigManager().getConfigObject().getSpawnLocation() != null) {
            String[] deserializedLocationString = plugin.getConfigManager().getConfigObject().getSpawnLocation().split(":");
            this.spawnLocation = new Location(plugin.getServer().getWorld(deserializedLocationString[0]), Double.parseDouble(deserializedLocationString[1]), Double.parseDouble(deserializedLocationString[2]), Double.parseDouble(deserializedLocationString[3]));
        } else
            this.spawnLocation = null;
        startScheduler();
    }

    public void updateVisibility(Player player, VisiblePlayer visiblePlayer) {
        switch (visiblePlayer.getVisibilityType()) {
            case ALL:
                plugin.getServer().getOnlinePlayers().forEach(target -> player.showPlayer(plugin, target));
                break;
            case VIP:
                plugin.getServer().getOnlinePlayers().forEach(target -> player.showPlayer(plugin, target));
                plugin.getServer().getOnlinePlayers().stream().filter(target -> !target.hasPermission("playerhider.vip")).forEach(target -> {
                    if (player != target && player.canSee(target))
                        player.hidePlayer(plugin, target);
                });
                break;
            case NONE:
                for (Player target : plugin.getServer().getOnlinePlayers()) {
                    if (player != target && player.canSee(target)) {
                        System.out.println(target.getName());
                        player.hidePlayer(plugin, target);
                    }
                }
                break;
        }
    }

    public VisiblePlayer getVisiblePlayer(UUID uuid) {
        return visiblePlayers.stream().filter(visiblePlayer -> visiblePlayer.getUuid().toString().matches(uuid.toString())).findFirst().orElse(new VisiblePlayer(uuid, VisibilityType.ALL));
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
        plugin.getConfigManager().setSpawnLocation(location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ());
    }

    private void startScheduler() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (plugin.getDataManager().getSpawnLocation() == null)
                return;
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                VisiblePlayer visiblePlayer = getVisiblePlayer(player.getUniqueId());
                if (plugin.getDataManager().getSpawnLocation().getWorld() != player.getLocation().getWorld()) {
                    if (visiblePlayer.getVisibilityType() != VisibilityType.ALL) {
                        visiblePlayer.setVisibilityType(VisibilityType.ALL);
                        updateVisibility(player, visiblePlayer);
                        player.sendMessage(plugin.getPrefix() + "Du siehst jetzt alle Spieler, da du dich zu weit vom Spawn entfernt hast.");
                    }
                    continue;
                }
                if (visiblePlayer.getVisibilityType() == VisibilityType.ALL || player.getLocation().distance(spawnLocation) <= plugin.getConfigManager().getConfigObject().getRadiusFromSpawn()) continue;
                visiblePlayer.setVisibilityType(VisibilityType.ALL);
                updateVisibility(player, visiblePlayer);
                player.sendMessage(plugin.getPrefix() + "Du siehst jetzt alle Spieler, da du dich zu weit vom Spawn entfernt hast.");
            }
        }, 0, 20);
    }

}
