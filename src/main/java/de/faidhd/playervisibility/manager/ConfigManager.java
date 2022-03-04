package de.faidhd.playervisibility.manager;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.faidhd.playervisibility.PlayerVisibilityPlugin;
import de.faidhd.playervisibility.objects.ConfigObject;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigManager {

    private final PlayerVisibilityPlugin plugin;

    private File file;
    private Gson gson;
    @Getter
    private ConfigObject configObject;

    public ConfigManager(PlayerVisibilityPlugin plugin) {
        this.plugin = plugin;
        initConfig();
    }

    @SneakyThrows
    private void initConfig() {
        this.file = new File(plugin.getDataFolder(), "config.json");
        this.gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        if (!file.exists()) {
            Files.createParentDirs(file);
            file.createNewFile();
            this.configObject = ConfigObject.builder().build();
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            writer.write(gson.toJson(configObject));
            writer.flush();
            writer.close();
        } else
            this.configObject = gson.fromJson(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8), ConfigObject.class);
    }

    @SneakyThrows
    public void setSpawnLocation(String serializedLocation) {
        configObject.setSpawnLocation(serializedLocation);
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        writer.write(gson.toJson(configObject));
        writer.flush();
        writer.close();
    }
}
