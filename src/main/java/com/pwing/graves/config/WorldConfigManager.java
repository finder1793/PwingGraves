package com.pwing.graves.config;

import org.bukkit.World;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class WorldConfigManager {
    private final Map<String, WorldConfig> worldConfigs = new HashMap<>();
    private final File dataFolder;

    public WorldConfigManager(File dataFolder) {
        this.dataFolder = dataFolder;
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
}
