package com.pwing.graves.listeners;

import com.pwing.graves.PwingGraves;
import com.pwing.graves.respawn.RespawnPoint;
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockBreakEvent;
import com.nexomc.nexo.api.events.custom_block.noteblock.NexoNoteBlockPlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;

public class NexoBlockListener implements Listener {
    private final PwingGraves plugin;

    public NexoBlockListener(PwingGraves plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onNexoBlockPlace(NexoNoteBlockPlaceEvent event) {
        if (!plugin.getConfig().getBoolean("settings.respawn-point-block.enabled", false)) {
            return; // Feature is disabled
        }

        Player player = event.getPlayer();
        String placedBlockId = event.getMechanic().getItemID(); // Use getMechanic() to retrieve the block ID
        String configuredBlockId = plugin.getConfig().getString("settings.respawn-point-block.material", "nexo:beacon");

        if (!placedBlockId.equalsIgnoreCase(configuredBlockId)) {
            return; // Block does not match the configured ID
        }

        String pointName = plugin.getConfig().getString("settings.respawn-point-block.name-prefix", "BlockRespawn_") + System.currentTimeMillis();
        RespawnPoint respawnPoint = new RespawnPoint(pointName, event.getBlock().getLocation());

        plugin.getRespawnManager().addRespawnPoint(player.getWorld().getName(), respawnPoint);
        player.sendMessage(plugin.getMessageManager().getMessage("respawn-point.added", "%name%", pointName));
    }

    @EventHandler
    public void onNexoBlockBreak(NexoNoteBlockBreakEvent event) {
        if (!plugin.getConfig().getBoolean("settings.respawn-point-block.enabled", false)) {
            return; // Feature is disabled
        }

        String brokenBlockId = event.getMechanic().getItemID(); // Use getMechanic() to retrieve the block ID
        String configuredBlockId = plugin.getConfig().getString("settings.respawn-point-block.material", "nexo:beacon");

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
}
