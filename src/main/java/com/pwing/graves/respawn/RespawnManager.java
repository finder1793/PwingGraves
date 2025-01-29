package com.pwing.graves.respawn;

import com.pwing.graves.PwingGraves;
import com.pwing.graves.config.WorldConfig;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import java.util.*;

public class RespawnManager {
    private final Map<String, Set<RespawnPoint>> worldRespawnPoints = new HashMap<>();
    private final PwingGraves plugin;

    public RespawnManager(PwingGraves plugin) {
        this.plugin = plugin;
    }

    public void saveRespawnPoints(String world) {
        WorldConfig worldConfig = plugin.getWorldConfigManager().getWorldConfig(world);
        Set<RespawnPoint> points = worldRespawnPoints.getOrDefault(world, new HashSet<>());
        worldConfig.getConfig().set("respawn-points", new ArrayList<>(points));
        worldConfig.save();
    }

    public void addRespawnPoint(String world, RespawnPoint point) {
        worldRespawnPoints.computeIfAbsent(world, k -> new HashSet<>()).add(point);
        saveRespawnPoints(world);
    }

    public void removeRespawnPoint(String world, String name) {
        worldRespawnPoints.getOrDefault(world, new HashSet<>())
                .removeIf(point -> point.getName().equals(name));
        saveRespawnPoints(world);
    }

    public Location getNearestRespawnPoint(Location deathLocation) {
        World world = deathLocation.getWorld();
        if (world == null || !worldRespawnPoints.containsKey(world.getName())) {
            return world.getSpawnLocation();
        }

        return worldRespawnPoints.get(world.getName()).stream()
                .min(Comparator.comparingDouble(point ->
                        point.getLocation().distanceSquared(deathLocation)))
                .map(RespawnPoint::getLocation)
                .orElse(world.getSpawnLocation());
    }

    public void loadRespawnPoints(String world) {
        WorldConfig worldConfig = plugin.getWorldConfigManager().getWorldConfig(world);
        List<?> points = worldConfig.getConfig().getList("respawn-points", new ArrayList<>());

        Set<RespawnPoint> respawnPoints = new HashSet<>();
        for (Object obj : points) {
            if (obj instanceof RespawnPoint point) {
                respawnPoints.add(point);
            }
        }

        worldRespawnPoints.put(world, respawnPoints);
    }

    public void loadAllRespawnPoints() {
        for (World world : plugin.getServer().getWorlds()) {
            loadRespawnPoints(world.getName());
        }
    }

    public int getTotalPoints() {
        return worldRespawnPoints.values().stream()
                .mapToInt(Set::size)
                .sum();
    }

    public String getNearestPointName(Location location) {
        RespawnPoint nearest = worldRespawnPoints.get(location.getWorld().getName()).stream()
                .min(Comparator.comparingDouble(point ->
                        point.getLocation().distanceSquared(location)))
                .orElse(null);
        return nearest != null ? nearest.getName() : "None";
    }

    public Set<RespawnPoint> getPointsInWorld(String world) {
        return worldRespawnPoints.getOrDefault(world, new HashSet<>());
    }


    public boolean pointExists(String name) {
        return worldRespawnPoints.values().stream()
                .flatMap(Set::stream)
                .anyMatch(point -> point.getName().equals(name));
    }


    public Set<RespawnPoint> getRespawnPoints(String worldName) {
        return worldRespawnPoints.getOrDefault(worldName, new HashSet<>());
    }

    public RespawnPoint getRespawnPoint(String worldName, String pointName) {
        Set<RespawnPoint> worldPoints = getRespawnPoints(worldName);
        return worldPoints.stream()
            .filter(point -> point.getName().equalsIgnoreCase(pointName))
            .findFirst()
            .orElse(null);
    }

    public boolean isPlayerAtRespawnPoint(Player player, String pointName) {
        Location playerLocation = player.getLocation();
        return worldRespawnPoints.getOrDefault(playerLocation.getWorld().getName(), new HashSet<>())
                .stream()
                .anyMatch(point -> point.getName().equals(pointName) &&
                        point.getLocation().distanceSquared(playerLocation) < 1);
    }

    public void teleportPlayerToRespawnPoint(Player player, String pointName) {
        RespawnPoint point = worldRespawnPoints.getOrDefault(player.getWorld().getName(), new HashSet<>())
                .stream()
                .filter(p -> p.getName().equals(pointName))
                .findFirst()
                .orElse(null);
        if (point != null) {
            player.teleport(point.getLocation());
        }
    }

    public Set<String> getWorldNames() {
        return worldRespawnPoints.keySet();
    }
}
