package com.pwing.graves.config;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class WorldConfigManager {
    private final Map<String, WorldConfig> worldConfigs = new HashMap<>();
    private final File dataFolder;

    public WorldConfigManager(File dataFolder) {
        this.dataFolder = dataFolder;
        loadWorldConfigs();
    }

    public WorldConfig getWorldConfig(String worldName) {
        return worldConfigs.computeIfAbsent(worldName, 
            name -> new WorldConfig(dataFolder, name));
    }

    public void loadWorld(World world) {
        getWorldConfig(world.getName());
    }

    public void saveAll() {
        worldConfigs.values().forEach(WorldConfig::save);
    }

    public Map<String, WorldConfig> getWorldConfigs() {
        return worldConfigs;
    }

    public void loadWorldConfigs() {
        File worldsFolder = new File(dataFolder, "worlds");
        if (worldsFolder.exists()) {
            File[] files = worldsFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".yml")) {
                        String worldName = file.getName().replace(".yml", "");
                        WorldConfig worldConfig = new WorldConfig(dataFolder, worldName);
                        worldConfigs.put(worldName, worldConfig);
                    }
                }
            }
        }
    }
}