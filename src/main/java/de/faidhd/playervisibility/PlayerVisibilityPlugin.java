package de.faidhd.playervisibility;

import de.faidhd.playervisibility.commands.PlayerVisibilityCommand;
import de.faidhd.playervisibility.listeners.PlayerJoinListener;
import de.faidhd.playervisibility.manager.ConfigManager;
import de.faidhd.playervisibility.manager.DataManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class PlayerVisibilityPlugin extends JavaPlugin {

    private String prefix;

    private ConfigManager configManager;
    private DataManager dataManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.prefix = ChatColor.translateAlternateColorCodes('&', configManager.getConfigObject().getPrefix());
        this.dataManager = new DataManager(this);

        registerListeners();
        registerCommands();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    private void registerCommands() {
        getCommand("playervisibility").setExecutor(new PlayerVisibilityCommand(this));
    }
}
