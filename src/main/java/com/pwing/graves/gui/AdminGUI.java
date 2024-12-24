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
    private static final String ADMIN_TITLE = ChatColor.RED + "Admin Control Panel";

    public AdminGUI(PwingGraves plugin) {
        this.plugin = plugin;
    }

    public void openAdminMenu(Player player) {
        if (!player.hasPermission("pwinggraves.admin")) {
            return;
        }

        Inventory gui = Bukkit.createInventory(null, 54, ADMIN_TITLE);

        // World Points Manager
        ItemStack worldPoints = createGuiItem(Material.BEACON,
            ChatColor.GOLD + "World Points Manager",
            ChatColor.GRAY + "Manage all world respawn points",
            ChatColor.YELLOW + "Left-click to view/edit",
            ChatColor.RED + "Right-click to remove");
        gui.setItem(20, worldPoints);

        // Create New Point
        ItemStack newPoint = createGuiItem(Material.EMERALD_BLOCK,
            ChatColor.GREEN + "Create New Point",
            ChatColor.GRAY + "Create a new respawn point",
            ChatColor.YELLOW + "Click to create at your location");
        gui.setItem(24, newPoint);

        // World Settings
        ItemStack worldSettings = createGuiItem(Material.COMMAND_BLOCK,
            ChatColor.AQUA + "World Settings",
            ChatColor.GRAY + "Configure world-specific settings");
        gui.setItem(31, worldSettings);

        // Statistics
        ItemStack stats = createGuiItem(Material.BOOK,
            ChatColor.LIGHT_PURPLE + "Statistics",
            ChatColor.GRAY + "View respawn point usage stats");
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
