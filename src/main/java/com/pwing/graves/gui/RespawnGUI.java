package com.pwing.graves.gui;

import com.pwing.graves.PwingGraves;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class RespawnGUI {
    private final PwingGraves plugin;
    private static final String GUI_TITLE = ChatColor.DARK_PURPLE + "Respawn Points";

    public RespawnGUI(PwingGraves plugin) {
        this.plugin = plugin;
    }

    public void openMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, GUI_TITLE);

        // Personal Points Section
        ItemStack personalPoints = createGuiItem(Material.PLAYER_HEAD, 
            ChatColor.GREEN + "Personal Points",
            ChatColor.GRAY + "Click to manage your personal respawn points",
            ChatColor.YELLOW + "You have: " + plugin.getPlayerManager().getPersonalRespawnPoints(player).size());
        gui.setItem(20, personalPoints);

        // World Points Section
        ItemStack worldPoints = createGuiItem(Material.COMPASS,
            ChatColor.AQUA + "World Points",
            ChatColor.GRAY + "Click to view available world respawn points");
        gui.setItem(24, worldPoints);

        // Quick Travel
        ItemStack quickTravel = createGuiItem(Material.ENDER_PEARL,
            ChatColor.LIGHT_PURPLE + "Quick Travel",
            ChatColor.GRAY + "Teleport to discovered respawn points");
        gui.setItem(31, quickTravel);

        // Categories
        ItemStack categories = createGuiItem(Material.BOOKSHELF,
            ChatColor.GOLD + "Categories",
            ChatColor.GRAY + "Browse respawn points by category");
        gui.setItem(13, categories);

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
