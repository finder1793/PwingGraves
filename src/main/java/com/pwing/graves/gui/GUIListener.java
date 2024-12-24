package com.pwing.graves.gui;

import com.pwing.graves.PwingGraves;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {
    private final PwingGraves plugin;

    public GUIListener(PwingGraves plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        
        if (!event.getView().getTitle().equals(ChatColor.DARK_PURPLE + "Respawn Points")) return;

        event.setCancelled(true);

        switch (event.getSlot()) {
            case 20 -> openPersonalPoints(player);
            case 24 -> openWorldPoints(player);
            case 31 -> openQuickTravel(player);
            case 13 -> openCategories(player);
        }
    }

    private void openPersonalPoints(Player player) {
        // Create personal points management GUI
    }

    private void openWorldPoints(Player player) {
        // Create world points view GUI
    }

    private void openQuickTravel(Player player) {
        // Create quick travel GUI
    }

    private void openCategories(Player player) {
        // Create categories GUI
    }
}
