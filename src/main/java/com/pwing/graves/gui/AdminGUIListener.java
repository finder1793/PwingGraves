package com.pwing.graves.gui;

import com.pwing.graves.PwingGraves;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.ClickType;
import com.pwing.graves.respawn.RespawnPoint;


public class AdminGUIListener implements Listener {
    private final PwingGraves plugin;

    public AdminGUIListener(PwingGraves plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        if (!event.getView().getTitle().equals(ChatColor.RED + "Admin Control Panel")) return;

        event.setCancelled(true);

        if (!player.hasPermission("pwinggraves.admin")) return;

        switch (event.getSlot()) {
            case 20 -> handleWorldPointsManager(player, event.getClick());
            case 24 -> createNewPoint(player);
            case 31 -> openWorldSettings(player);
            case 13 -> openStatistics(player);
        }
    }

    private void handleWorldPointsManager(Player player, ClickType clickType) {
        if (clickType.isRightClick()) {
            // Open removal confirmation GUI
            openRemovalGUI(player);
        } else {
            // Open edit GUI
            openEditGUI(player);
        }
    }

    private void createNewPoint(Player player) {
        player.closeInventory();
        // Start creation process
        plugin.getRespawnManager().addRespawnPoint(
            player.getWorld().getName(),
            new RespawnPoint("point_" + System.currentTimeMillis(), player.getLocation())
        );
        player.sendMessage(ChatColor.GREEN + "New respawn point created at your location!");
    }

    private void openWorldSettings(Player player) {
        // Implement world settings GUI
    }

    private void openStatistics(Player player) {
        // Implement statistics GUI
    }

    private void openRemovalGUI(Player player) {
        // Implement removal confirmation GUI
    }

    private void openEditGUI(Player player) {
        // Implement edit GUI
    }
}


