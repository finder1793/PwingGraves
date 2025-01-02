package com.pwing.graves.gui;

import com.pwing.graves.PwingGraves;
import com.pwing.graves.respawn.RespawnPoint;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class AdminGUI {
    private final PwingGraves plugin;
    private String getAdminTitle() {
        return plugin.getMessageManager().getMessage("gui.admin-title");
    }

    public AdminGUI(PwingGraves plugin) {
        this.plugin = plugin;
    }

    public void openAdminMenu(Player player) {
        if (!player.hasPermission("pwinggraves.admin")) {
            return;
        }

        Inventory gui = Bukkit.createInventory(null, 54, plugin.getMessageManager().getMessage("gui.admin.title"));

        // World Points Manager
        ItemStack worldPoints = createGuiItem(Material.BEACON,
            plugin.getMessageManager().getMessage("gui.admin.world-points.title"),
            plugin.getMessageManager().getMessage("gui.admin.world-points.lore"));
        gui.setItem(20, worldPoints);

        // Create New Point
        ItemStack newPoint = createGuiItem(Material.EMERALD_BLOCK,
            plugin.getMessageManager().getMessage("gui.admin.new-point.title"),
            plugin.getMessageManager().getMessage("gui.admin.new-point.lore"));
        gui.setItem(24, newPoint);

        // World Settings
        ItemStack worldSettings = createGuiItem(Material.COMMAND_BLOCK,
            plugin.getMessageManager().getMessage("gui.admin.world-settings.title"),
            plugin.getMessageManager().getMessage("gui.admin.world-settings.lore"));
        gui.setItem(31, worldSettings);

        // Statistics
        ItemStack stats = createGuiItem(Material.BOOK,
            plugin.getMessageManager().getMessage("gui.admin.statistics.title"),
            plugin.getMessageManager().getMessage("gui.admin.statistics.lore"));
        gui.setItem(13, stats);

        player.openInventory(gui);
    }

    private ItemStack createGuiItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }
}
