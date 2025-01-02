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

    public RespawnGUI(PwingGraves plugin) {
        this.plugin = plugin;
    }

    public void openMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, plugin.getMessageManager().getMessage("gui.respawn-title"));

        // Personal Points Section
        ItemStack personalPoints = createGuiItem(Material.PLAYER_HEAD, 
            plugin.getMessageManager().getMessage("gui.personal-points.title"),
            plugin.getMessageManager().getMessage("gui.personal-points.lore",
                "%count%", plugin.getPlayerManager().getPersonalRespawnPoints(player).size()));
        gui.setItem(20, personalPoints);

        // World Points Section
        ItemStack worldPoints = createGuiItem(Material.COMPASS,
            plugin.getMessageManager().getMessage("gui.world-points.title"),
            plugin.getMessageManager().getMessage("gui.world-points.lore"));
        gui.setItem(24, worldPoints);

        // Quick Travel
        ItemStack quickTravel = createGuiItem(Material.ENDER_PEARL,
            plugin.getMessageManager().getMessage("gui.quick-travel.title"),
            plugin.getMessageManager().getMessage("gui.quick-travel.lore"));
        gui.setItem(31, quickTravel);

        // Categories
        ItemStack categories = createGuiItem(Material.BOOKSHELF,
            plugin.getMessageManager().getMessage("gui.categories.title"),
            plugin.getMessageManager().getMessage("gui.categories.lore"));
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
