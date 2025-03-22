package com.pwing.graves.listeners;

import com.pwing.graves.PwingGraves;
import com.pwing.graves.respawn.RespawnPoint;
import com.nexomc.nexo.api.events.furniture.NexoFurnitureBreakEvent;
import com.nexomc.nexo.api.events.furniture.NexoFurniturePlaceEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NexoFurnitureListener implements Listener {
    private final PwingGraves plugin;

    public NexoFurnitureListener(PwingGraves plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFurniturePlace(NexoFurniturePlaceEvent event) {
        if (!plugin.getConfig().getBoolean("settings.respawn-point-block.enabled", false)) {
            return; // Feature is disabled
        }

        Player player = event.getPlayer();
        String placedFurnitureId = event.getMechanic().getItemID(); // Use mechanic to get the furniture ID
        String configuredFurnitureId = plugin.getConfig().getString("settings.respawn-point-block.material", "nexo:beacon");

        if (!placedFurnitureId.equalsIgnoreCase(configuredFurnitureId)) {
            return; // Furniture does not match the configured ID
        }

        String pointName = plugin.getConfig().getString("settings.respawn-point-block.name-prefix", "FurnitureRespawn_") + System.currentTimeMillis();
        RespawnPoint respawnPoint = new RespawnPoint(pointName, event.getBaseEntity().getLocation()); // Use baseEntity for location

        plugin.getRespawnManager().addRespawnPoint(player.getWorld().getName(), respawnPoint);
        player.sendMessage(plugin.getMessageManager().getMessage("respawn-point.added", "%name%", pointName));
    }

    @EventHandler
    public void onFurnitureBreak(NexoFurnitureBreakEvent event) {
        if (!plugin.getConfig().getBoolean("settings.respawn-point-block.enabled", false)) {
            return; // Feature is disabled
        }

        String brokenFurnitureId = event.getMechanic().getItemID(); // Use mechanic to get the furniture ID
        String configuredFurnitureId = plugin.getConfig().getString("settings.respawn-point-block.material", "nexo:beacon");

        if (!brokenFurnitureId.equalsIgnoreCase(configuredFurnitureId)) {
            return; // Furniture does not match the configured ID
        }

        RespawnPoint point = plugin.getRespawnManager().getRespawnPoints(event.getBaseEntity().getLocation().getWorld().getName()).stream()
            .filter(p -> p.getLocation().equals(event.getBaseEntity().getLocation())) // Use baseEntity for location
            .findFirst()
            .orElse(null);

        if (point != null) {
            plugin.getRespawnManager().removeRespawnPoint(event.getBaseEntity().getLocation().getWorld().getName(), point.getName());
            event.getPlayer().sendMessage(plugin.getMessageManager().getMessage("respawn-point.removed", "%name%", point.getName()));
        }
    }
}
