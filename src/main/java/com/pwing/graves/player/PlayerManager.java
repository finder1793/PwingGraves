package com.pwing.graves.player;

import com.pwing.graves.PwingGraves;
import com.pwing.graves.respawn.RespawnPoint;
import org.bukkit.entity.Player;
import org.bukkit.World;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import java.util.*;
import java.io.File;
import java.util.stream.Collectors;

public class PlayerManager {
    private final PwingGraves plugin;
    private final PlayerDataManager dataManager;
    private final Map<UUID, Set<RespawnPoint>> personalRespawnPoints = new HashMap<>();
    private final Map<UUID, Long> respawnCooldowns = new HashMap<>();

    public PlayerManager(PwingGraves plugin) {
        this.plugin = plugin;
        this.dataManager = new PlayerDataManager(plugin.getDataFolder());
    }

    public int getMaxPersonalPoints(Player player) {
        // Check direct point permissions first
        for (int i = 50; i > 0; i--) {
            if (player.hasPermission("pwinggraves.personal.points." + i)) {
                return i;
            }
        }

        // Check group permissions
        ConfigurationSection groups = plugin.getConfig().getConfigurationSection("personal-respawns.groups");
        if (groups != null) {
            for (String groupName : groups.getKeys(false)) {
                if (player.hasPermission("pwinggraves.group." + groupName)) {
                    return groups.getInt(groupName + ".max-points");
                }
            }
        }

        return plugin.getConfig().getInt("personal-respawns.default-max-points", 3);
    }

    public boolean canCreatePersonalPointInWorld(World world) {
        List<String> disabledWorlds = plugin.getConfig().getStringList("personal-respawns.disabled-worlds");
        return !disabledWorlds.contains(world.getName());
    }

    public void addPersonalRespawnPoint(Player player, RespawnPoint point) {
        if (!canCreatePersonalPointInWorld(player.getWorld())) {
            player.sendMessage(ChatColor.RED + "Personal respawn points are disabled in this world!");
            return;
        }

        Set<RespawnPoint> points = getPersonalRespawnPoints(player);
        if (points.size() >= getMaxPersonalPoints(player)) {
            player.sendMessage(ChatColor.RED + "You have reached your maximum number of personal respawn points!");
            return;
        }

        points.add(point);
        savePlayerRespawnPoints(player.getUniqueId(), points);
    }

    public Set<RespawnPoint> getPersonalRespawnPoints(Player player) {
        return personalRespawnPoints.getOrDefault(player.getUniqueId(), new HashSet<>());
    }

    public boolean canUseRespawnPoint(Player player, RespawnPoint point) {
        if (!player.hasPermission("pwinggraves.use." + point.getName())) {
            return false;
        }

        long cooldownTime = plugin.getConfig().getLong("cooldowns.respawn", 0) * 1000;
        if (cooldownTime > 0) {
            long lastUse = respawnCooldowns.getOrDefault(player.getUniqueId(), 0L);
            if (System.currentTimeMillis() - lastUse < cooldownTime) {
                return false;
            }
        }

        return true;
    }

    public void setRespawnCooldown(Player player) {
        respawnCooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private void savePlayerRespawnPoints(UUID uuid, Set<RespawnPoint> points) {
        YamlConfiguration config = dataManager.getPlayerData(uuid);
        config.set("respawn-points", new ArrayList<>(points));
        dataManager.savePlayerData(uuid, config);
    }

    public void loadAllPlayerData() {
        File playerDataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (playerDataFolder.exists()) {
            for (File file : playerDataFolder.listFiles()) {
                String uuidString = file.getName().replace(".yml", "");
                UUID uuid = UUID.fromString(uuidString);
                YamlConfiguration config = dataManager.getPlayerData(uuid);

                if (config.contains("respawn-points")) {
                    List<?> points = config.getList("respawn-points");
                    Set<RespawnPoint> respawnPoints = points.stream()
                            .filter(p -> p instanceof RespawnPoint)
                            .map(p -> (RespawnPoint) p)
                            .collect(Collectors.toSet());
                    personalRespawnPoints.put(uuid, respawnPoints);
                }
            }
        }
    }
}
