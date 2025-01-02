package com.pwing.graves.utils;

import com.pwing.graves.PwingGraves;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.InputStreamReader;

public class MessageManager {
    private final Map<String, String> messages = new HashMap<>();
    private final PwingGraves plugin;
    private String prefix;

    public MessageManager(PwingGraves plugin) {
        this.plugin = plugin;
        loadMessages();
    }

    public void loadMessages() {
        File messageFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messageFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(messageFile);
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
            new InputStreamReader(plugin.getResource("messages.yml"))
        );

        // Add any missing keys from default config
        for (String key : defaultConfig.getKeys(true)) {
            if (!config.contains(key)) {
                config.set(key, defaultConfig.get(key));
            }
        }

        // Save updated config
        try {
            config.save(messageFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save updated messages.yml");
        }

        // Load all messages into memory
        prefix = config.getString("messages.prefix", "&6[PwingGraves] &r");
        for (String key : config.getConfigurationSection("messages").getKeys(true)) {
            String value = config.getString("messages." + key);
            if (value != null) {
                messages.put(key, value);
            }
        }
    }

    public String getMessage(String path, Object... replacements) {
        String message = messages.getOrDefault(path, path);
        message = message.replace("%prefix%", prefix);

        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 < replacements.length) {
                message = message.replace(String.valueOf(replacements[i]),
                    String.valueOf(replacements[i + 1]));
            }
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
