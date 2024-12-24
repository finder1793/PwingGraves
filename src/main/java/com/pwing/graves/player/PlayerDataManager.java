package com.pwing.graves.player;

import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.UUID;

public class PlayerDataManager {
    private final File playerDataFolder;

    public PlayerDataManager(File dataFolder) {
        this.playerDataFolder = new File(dataFolder, "playerdata");
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }
    }

    public YamlConfiguration getPlayerData(UUID uuid) {
        File playerFile = new File(playerDataFolder, uuid.toString() + ".yml");
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create player data file", e);
            }
        }
        return YamlConfiguration.loadConfiguration(playerFile);
    }

    public void savePlayerData(UUID uuid, YamlConfiguration config) {
        try {
            config.save(new File(playerDataFolder, uuid.toString() + ".yml"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to save player data", e);
        }
    }
}
