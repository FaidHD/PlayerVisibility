package de.faidhd.playervisibility.commands;

import de.faidhd.playervisibility.PlayerVisibilityPlugin;
import de.faidhd.playervisibility.objects.VisibilityType;
import de.faidhd.playervisibility.objects.VisiblePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PlayerVisibilityCommand implements CommandExecutor {

    private final PlayerVisibilityPlugin plugin;

    public PlayerVisibilityCommand(PlayerVisibilityPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getPrefix() + "Du musst ein Spieler sein, um diesen Befehl nutzen zu können.");
            return false;
        }

        Player player = (Player) sender;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("setspawn")) {
                if (player.hasPermission("playerhider.setspawn")) {
                    plugin.getDataManager().setSpawnLocation(player.getLocation());
                    player.sendMessage(plugin.getPrefix() + "Du hast den Spawnpunkt markiert.");
                } else
                    player.sendMessage(plugin.getPrefix() + "Bitte benutze: /" + label + " <" + Arrays.stream(VisibilityType.values()).map(VisibilityType::name).collect(Collectors.joining(", ")) + ">");
                return false;
            }
            if (plugin.getDataManager().getSpawnLocation() == null) {
                player.sendMessage(plugin.getPrefix() + "Die Spawnlocation wurde noch nicht gesetzt.");
                return false;
            }
            if (plugin.getDataManager().getSpawnLocation().distance(player.getLocation()) > plugin.getConfigManager().getConfigObject().getRadiusFromSpawn()) {
                player.sendMessage(plugin.getPrefix() + "Dieses Feature ist für den Spawn vorbehalten. Außerhalb des Spawns siehst du alle Spieler.");
                return false;
            }
            VisibilityType type = VisibilityType.getVisibilityTypeByName(args[0]);
            if (type == null) {
                player.sendMessage(plugin.getPrefix() + "Bitte benutze: /" + label + " <" + Arrays.stream(VisibilityType.values()).map(VisibilityType::name).collect(Collectors.joining(", ")) + ">");
                return false;
            }
            VisiblePlayer visiblePlayer = plugin.getDataManager().getVisiblePlayer(player.getUniqueId());
            visiblePlayer.setVisibilityType(type);
            plugin.getDataManager().updateVisibility(player, visiblePlayer);
            player.sendMessage(plugin.getPrefix() + "Du siehst nun folgende Spieler: " + type.name());
        } else
            player.sendMessage(plugin.getPrefix() + "Bitte benutze: /" + label + " <" + Arrays.stream(VisibilityType.values()).map(VisibilityType::name).collect(Collectors.joining(", ")) + ">");

        return false;
    }
}
