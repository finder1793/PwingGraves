package com.pwing.graves;

import com.pwing.graves.commands.GravesCommand;
import com.pwing.graves.config.WorldConfigManager;
import com.pwing.graves.respawn.RespawnManager;
import com.pwing.graves.respawn.RespawnPoint;
import com.pwing.graves.gui.RespawnGUI;
import com.pwing.graves.gui.AdminGUI;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.util.List;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.pwing.graves.player.PlayerManager;
import com.pwing.graves.economy.RespawnEconomy;
import com.pwing.graves.listeners.BlockListener;
import com.pwing.graves.listeners.NexoFurnitureListener;
import com.pwing.graves.listeners.BlockPlaceListener;
import com.pwing.graves.listeners.NexoBlockListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import com.pwing.graves.integrations.skript.SkriptIntegration;
import ch.njol.skript.Skript;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.Arrays;
import com.pwing.graves.utils.MessageManager;
import java.io.File;
import com.pwing.graves.config.WorldConfig;
import com.pwing.graves.integrations.skript.EffectCreatePoint;
import com.pwing.graves.integrations.skript.CondRespawnPointExists;
import org.bukkit.ChatColor;
import org.bukkit.event.EventPriority;

public final class PwingGraves extends JavaPlugin implements Listener {

    private RespawnManager respawnManager;
    private WorldConfigManager worldConfigManager;
    private RespawnGUI respawnGUI;
    private AdminGUI adminGUI;
    private PlayerManager playerManager;
    private RespawnEconomy respawnEconomy;
    private MessageManager messageManager;
    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "----------------------------------------");
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "PwingGraves " + ChatColor.WHITE + "v" + getDescription().getVersion());
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "Created by: " + ChatColor.WHITE + "Finder17");
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "Status: " + ChatColor.GREEN + "Enabled");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "----------------------------------------");

        ConfigurationSerialization.registerClass(RespawnPoint.class);

        worldConfigManager = new WorldConfigManager(getDataFolder());
        worldConfigManager.loadWorldConfigs();
        respawnManager = new RespawnManager(this);
        respawnGUI = new RespawnGUI(this);
        adminGUI = new AdminGUI(this);
        playerManager = new PlayerManager(this);
        playerManager.loadAllPlayerData();
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("graves").setExecutor(new GravesCommand(this));

        saveDefaultConfig();
        messageManager = new MessageManager(this);
        loadRespawnPoints();

        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                Economy economy = rsp.getProvider();
                double createCost = getConfig().getDouble("economy.costs.point-creation");
                double teleportCost = getConfig().getDouble("economy.costs.teleport");
                double respawnCost = getConfig().getDouble("economy.costs.respawn", 100.0); // Default to 100.0 if not set
                respawnEconomy = new RespawnEconomy(economy, createCost, teleportCost, respawnCost);
            }
        }

        registerSkript();
        SkriptIntegration.registerEffects();
        getLogger().info("PwingGraves plugin enabled successfully.");
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);

        if (getServer().getPluginManager().getPlugin("Nexo") != null) {
            getLogger().info("Nexo detected. Registering NexoFurnitureListener...");
            getServer().getPluginManager().registerEvents(new NexoFurnitureListener(this), this);
        } else {
            getLogger().info("Nexo not detected. Skipping NexoFurnitureListener registration.");
        }
    }



    public MessageManager getMessageManager() {
        return messageManager;
    }
    @Override
    public void onDisable() {
        worldConfigManager.saveAll();
        getLogger().info("PwingGraves has been disabled!");
        getLogger().info("PwingGraves plugin disabled successfully.");
    }

    public AdminGUI getAdminGUI() {
        return adminGUI;
    }

    public RespawnGUI getRespawnGUI() {
        return respawnGUI;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (player.getLastDeathLocation() != null) {
            Location respawnLocation = respawnManager.getNearestRespawnPoint(player.getLastDeathLocation());
            if (respawnEconomy != null && !respawnEconomy.chargeForRespawn(player)) {
                respawnLocation = player.getWorld().getSpawnLocation(); // Default to world spawn
            }
            event.setRespawnLocation(respawnLocation);
        }
    }

    private void loadRespawnPoints() {
        getLogger().info("Starting to load respawn points from world configs...");
        getLogger().info("Available worlds: " + worldConfigManager.getWorldConfigs().keySet());

        File worldsFolder = new File(getDataFolder(), "worlds");
        if (worldsFolder.exists()) {
            getLogger().info("Worlds folder contents: " + Arrays.toString(worldsFolder.list()));
        }

        for (String worldName : worldConfigManager.getWorldConfigs().keySet()) {
            WorldConfig worldConfig = worldConfigManager.getWorldConfig(worldName);
            getLogger().info("Checking world config for: " + worldName);

            if (worldConfig.getConfig().contains("respawn-points")) {
                List<?> points = worldConfig.getConfig().getList("respawn-points");
                if (points != null && !points.isEmpty()) {
                    getLogger().info("Found " + points.size() + " respawn points in " + worldName);

                    points.forEach(point -> {
                        if (point instanceof RespawnPoint respawnPoint) {
                            respawnManager.addRespawnPoint(worldName, respawnPoint);
                            getLogger().info("✓ Loaded respawn point: " + respawnPoint.getName() +
                                           " at location: " + respawnPoint.getLocation().toString());
                        } else {
                            getLogger().warning("× Failed to load point - invalid type: " + point.getClass().getName());
                        }
                    });
                } else {
                    getLogger().info("No respawn points found in " + worldName);
                }
            }
        }

        getLogger().info("Completed respawn point loading process");
    }    public RespawnManager getRespawnManager() {
        return respawnManager;
    }

    public WorldConfigManager getWorldConfigManager() {
        return worldConfigManager;
    }

    private void registerSkript() {
        if (getServer().getPluginManager().getPlugin("Skript") != null) {
            getLogger().info("Registering Skript support...");
            EffectCreatePoint.setPlugin(this);
            CondRespawnPointExists.setPlugin(this);
        }
    }
}





