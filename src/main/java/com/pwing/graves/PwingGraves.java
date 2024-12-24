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
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import com.pwing.graves.player.PlayerManager;
import com.pwing.graves.economy.RespawnEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import ch.njol.skript.Skript;
import com.pwing.graves.integrations.skript.EffectCreatePoint;
import com.pwing.graves.integrations.skript.CondRespawnPointExists;


public class PwingGraves extends JavaPlugin implements Listener {
    
    private RespawnManager respawnManager;
    private WorldConfigManager worldConfigManager;
    private RespawnGUI respawnGUI;
    private AdminGUI adminGUI;
    private PlayerManager playerManager;
    private RespawnEconomy respawnEconomy;
    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "----------------------------------------");
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "PwingGraves " + ChatColor.WHITE + "v" + getDescription().getVersion());
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "Created by: " + ChatColor.WHITE + "Finder17");
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "Status: " + ChatColor.GREEN + "Enabled");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "----------------------------------------");
        
        ConfigurationSerialization.registerClass(RespawnPoint.class);
        
        worldConfigManager = new WorldConfigManager(getDataFolder());
        respawnManager = new RespawnManager(this);
        respawnGUI = new RespawnGUI(this);
        adminGUI = new AdminGUI(this);
        playerManager = new PlayerManager(this);
        
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("graves").setExecutor(new GravesCommand(this));
        
        saveDefaultConfig();
        loadRespawnPoints();
        
        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                Economy economy = rsp.getProvider();
                double createCost = getConfig().getDouble("economy.costs.point-creation");
                double teleportCost = getConfig().getDouble("economy.costs.teleport");
                respawnEconomy = new RespawnEconomy(economy, createCost, teleportCost);
            }
        }
    }
    }
    @Override
    public void onDisable() {
        worldConfigManager.saveAll();
        getLogger().info("PwingGraves has been disabled!");
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
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (event.getPlayer().getLastDeathLocation() != null) {
            event.setRespawnLocation(respawnManager.getNearestRespawnPoint(event.getPlayer().getLastDeathLocation()));
        }
    }

    private void loadRespawnPoints() {
        if (getConfig().contains("respawn-points")) {
            getConfig().getConfigurationSection("respawn-points").getKeys(false).forEach(world -> {
                getConfig().getList("respawn-points." + world).forEach(point -> {
                    RespawnPoint respawnPoint = (RespawnPoint) point;
                    respawnManager.addRespawnPoint(world, respawnPoint);
                });
            });
        }
    }

    public RespawnManager getRespawnManager() {
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
            // Register effects and conditions
            Skript.registerEffect(EffectCreatePoint.class);
            Skript.registerCondition(CondRespawnPointExists.class);
        }
    }
}
