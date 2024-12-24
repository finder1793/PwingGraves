package com.pwing.graves.commands;

import com.pwing.graves.PwingGraves;
import com.pwing.graves.respawn.RespawnPoint;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GravesCommand implements CommandExecutor {
    private final PwingGraves plugin;

    public GravesCommand(PwingGraves plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }

        if (!player.hasPermission("pwinggraves.admin")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                plugin.getConfig().getString("messages.no-permission", "&cNo permission!")));
            return true;
        }

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "admin" -> {
                if (player.hasPermission("pwinggraves.admin")) {
                    plugin.getAdminGUI().openAdminMenu(player);
                }
            }
            case "menu" -> {
                if (player.hasPermission("pwinggraves.menu")) {
                    plugin.getRespawnGUI().openMainMenu(player);
                }
            }
            case "add" -> {
                if (args.length < 2) {
                    sendHelp(player);
                    return true;
                }
                RespawnPoint point = new RespawnPoint(args[1], player.getLocation());
                plugin.getRespawnManager().addRespawnPoint(player.getWorld().getName(), point);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.respawn-point-added", "&aRespawn point added!")
                        .replace("%name%", args[1])));
            }
            case "remove" -> {
                if (args.length < 2) {
                    sendHelp(player);
                    return true;
                }
                plugin.getRespawnManager().removeRespawnPoint(player.getWorld().getName(), args[1]);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.respawn-point-removed", "&cRespawn point removed!")
                        .replace("%name%", args[1])));
            }
            default -> sendHelp(player);
        }
        return true;
    }
    private void sendHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "PwingGraves Commands:");
        player.sendMessage(ChatColor.YELLOW + "/graves add <name> " + ChatColor.GRAY + "- Add a respawn point");
        player.sendMessage(ChatColor.YELLOW + "/graves remove <name> " + ChatColor.GRAY + "- Remove a respawn point");
    }
}
