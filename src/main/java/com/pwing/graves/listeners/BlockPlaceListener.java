package com.pwing.graves.listeners;

import com.pwing.graves.PwingGraves;
import com.pwing.graves.respawn.RespawnPoint;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.entity.Player;

public class BlockPlaceListener implements Listener {
    private final PwingGraves plugin;

    public BlockPlaceListener(PwingGraves plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!plugin.getConfig().getBoolean("settings.respawn-point-block.enabled", false)) {
            return; // Feature is disabled
        }

        Player player = event.getPlayer();
        Material placedMaterial = event.getBlockPlaced().getType();
        Material configuredMaterial = Material.matchMaterial(plugin.getConfig().getString("settings.respawn-point-block.material", "BEACON"));

        if (configuredMaterial == null || placedMaterial != configuredMaterial) {
            return; // Block does not match the configured material
        }

        String pointName = plugin.getConfig().getString("settings.respawn-point-block.name-prefix", "BlockRespawn_") + System.currentTimeMillis();
        RespawnPoint respawnPoint = new RespawnPoint(pointName, event.getBlockPlaced().getLocation());

        plugin.getRespawnManager().addRespawnPoint(player.getWorld().getName(), respawnPoint);
        player.sendMessage(plugin.getMessageManager().getMessage("respawn-point.added", "%name%", pointName));
    }
}
