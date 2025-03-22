package com.pwing.graves.listeners;

import com.pwing.graves.PwingGraves;
import com.pwing.graves.respawn.RespawnPoint;
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockBreakEvent;
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockPlaceEvent;
import com.nexomc.nexo.api.events.furniture.NexoFurniturePlaceEvent;
import com.nexomc.nexo.api.events.furniture.NexoFurnitureBreakEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PersonalRespawnListener implements Listener {
    private final PwingGraves plugin;

    public PersonalRespawnListener(PwingGraves plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!plugin.getConfig().getBoolean("settings.personal-respawn-material.enabled", false)) {
            return; // Feature is disabled
        }

        Player player = event.getPlayer();
        Material configuredMaterial = Material.matchMaterial(plugin.getConfig().getString("settings.personal-respawn-material.material", "RESPAWN_ANCHOR"));
        boolean isNexoMaterial = plugin.getConfig().getBoolean("settings.personal-respawn-material.nexo-material", false);

        if (!isNexoMaterial) {
            if (event.getClickedBlock() == null || event.getClickedBlock().getType() != configuredMaterial) {
                return; // Not the configured vanilla material
            }

            String pointName = "PersonalRespawn_" + System.currentTimeMillis();
            RespawnPoint respawnPoint = new RespawnPoint(pointName, event.getClickedBlock().getLocation());

            plugin.getRespawnManager().addRespawnPoint(player.getWorld().getName(), respawnPoint);
            player.sendMessage(plugin.getMessageManager().getMessage("respawn-point.added", "%name%", pointName));
            event.setCancelled(true); // Prevent default interaction
        }
    }

    @EventHandler
    public void onNexoBlockPlace(NexoNoteBlockPlaceEvent event) {
        if (!plugin.getConfig().getBoolean("settings.personal-respawn-material.enabled", false)) {
            return; // Feature is disabled
        }

        Player player = event.getPlayer();
        String placedBlockId = event.getMechanic().getItemID();
        String configuredBlockId = plugin.getConfig().getString("settings.personal-respawn-material.material", "nexo:beacon");

        if (!placedBlockId.equalsIgnoreCase(configuredBlockId)) {
            return; // Block does not match the configured ID
        }

        String pointName = "PersonalRespawn_" + System.currentTimeMillis();
        RespawnPoint respawnPoint = new RespawnPoint(pointName, event.getBlock().getLocation());

        plugin.getRespawnManager().addRespawnPoint(player.getWorld().getName(), respawnPoint);
        player.sendMessage(plugin.getMessageManager().getMessage("respawn-point.added", "%name%", pointName));
    }

    @EventHandler
    public void onNexoBlockBreak(NexoNoteBlockBreakEvent event) {
        if (!plugin.getConfig().getBoolean("settings.personal-respawn-material.enabled", false)) {
            return; // Feature is disabled
        }

        String brokenBlockId = event.getMechanic().getItemID();
        String configuredBlockId = plugin.getConfig().getString("settings.personal-respawn-material.material", "nexo:beacon");

        if (!brokenBlockId.equalsIgnoreCase(configuredBlockId)) {
            return; // Block does not match the configured ID
        }

        RespawnPoint point = plugin.getRespawnManager().getRespawnPoints(event.getBlock().getWorld().getName()).stream()
            .filter(p -> p.getLocation().equals(event.getBlock().getLocation()))
            .findFirst()
            .orElse(null);

        if (point != null) {
            plugin.getRespawnManager().removeRespawnPoint(event.getBlock().getWorld().getName(), point.getName());
            event.getPlayer().sendMessage(plugin.getMessageManager().getMessage("respawn-point.removed", "%name%", point.getName()));
        }
    }

    @EventHandler
    public void onNexoFurniturePlace(NexoFurniturePlaceEvent event) {
        if (!plugin.getConfig().getBoolean("settings.personal-respawn-material.enabled", false)) {
            return; // Feature is disabled
        }

        Player player = event.getPlayer();
        String configuredFurnitureId = plugin.getConfig().getString("settings.personal-respawn-material.material", "nexo:beacon");

        if (!event.getMechanic().getItemID().equalsIgnoreCase(configuredFurnitureId)) {
            return; // Not the configured Nexo furniture
        }

        String pointName = "PersonalRespawn_" + System.currentTimeMillis();
        RespawnPoint respawnPoint = new RespawnPoint(pointName, event.getBaseEntity().getLocation());

        plugin.getRespawnManager().addRespawnPoint(player.getWorld().getName(), respawnPoint);
        player.sendMessage(plugin.getMessageManager().getMessage("respawn-point.added", "%name%", pointName));
    }

    @EventHandler
    public void onNexoFurnitureBreak(NexoFurnitureBreakEvent event) {
        if (!plugin.getConfig().getBoolean("settings.personal-respawn-material.enabled", false)) {
            return; // Feature is disabled
        }

        String configuredFurnitureId = plugin.getConfig().getString("settings.personal-respawn-material.material", "nexo:beacon");

        if (!event.getMechanic().getItemID().equalsIgnoreCase(configuredFurnitureId)) {
            return; // Not the configured Nexo furniture
        }

        RespawnPoint point = plugin.getRespawnManager().getRespawnPoints(event.getBaseEntity().getLocation().getWorld().getName()).stream()
            .filter(p -> p.getLocation().equals(event.getBaseEntity().getLocation()))
            .findFirst()
            .orElse(null);

        if (point != null) {
            plugin.getRespawnManager().removeRespawnPoint(event.getBaseEntity().getLocation().getWorld().getName(), point.getName());
            event.getPlayer().sendMessage(plugin.getMessageManager().getMessage("respawn-point.removed", "%name%", point.getName()));
        }
    }
}
