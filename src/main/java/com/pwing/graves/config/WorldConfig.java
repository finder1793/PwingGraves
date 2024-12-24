package com.pwing.graves.config;

import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class WorldConfig {
    private final File configFile;
    private YamlConfiguration config;
    private final String worldName;

    public WorldConfig(File dataFolder, String worldName) {
        this.worldName = worldName;
        File worldsFolder = new File(dataFolder, "worlds");
        if (!worldsFolder.exists()) {
            worldsFolder.mkdirs();
        }
        this.configFile = new File(worldsFolder, worldName + ".yml");
        loadConfig();
    }

    private void loadConfig() {
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Failed to create world config for " + worldName, e);
            }
        }
        this.config = YamlConfiguration.loadConfiguration(configFile);
        setDefaults();
    }

    private void setDefaults() {
        config.addDefault("enabled", true);
        config.addDefault("use-world-spawn-if-no-points", true);
        config.options().copyDefaults(true);
        save();
    }

    public void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save world config for " + worldName, e);
        }
    }

    public YamlConfiguration getConfig() {
        return config;
    }
}
