package com.pwing.graves.commands;

import com.pwing.graves.PwingGraves;
import com.pwing.graves.respawn.RespawnPoint;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Set;

public class GravesCommand implements CommandExecutor {
    private final PwingGraves plugin;

    public GravesCommand(PwingGraves plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "list":
                if (!sender.hasPermission("pwinggraves.list")) {
                    sender.sendMessage(plugin.getMessageManager().getMessage("permission.list"));
                    return true;
                }

                Set<RespawnPoint> points = plugin.getRespawnManager().getRespawnPoints(((Player)sender).getWorld().getName());
                if (points.isEmpty()) {
                    sender.sendMessage(plugin.getMessageManager().getMessage("respawn-point.list-empty"));
                    return true;
                }

                sender.sendMessage(plugin.getMessageManager().getMessage("respawn-point.list-header", "%world%", ((Player)sender).getWorld().getName()));
                points.forEach(point -> sender.sendMessage(plugin.getMessageManager().getMessage("respawn-point.list-format",
                    "%name%", point.getName(),
                    "%x%", String.valueOf(point.getLocation().getBlockX()),
                    "%y%", String.valueOf(point.getLocation().getBlockY()),
                    "%z%", String.valueOf(point.getLocation().getBlockZ()))));
                return true;

            case "tp":
                if (!sender.hasPermission("pwinggraves.tp")) {
                    sender.sendMessage(plugin.getMessageManager().getMessage("permission.tp"));
                    return true;
                }

                if (args.length < 2) {
                    sender.sendMessage(plugin.getMessageManager().getMessage("commands.usage"));
                    return true;
                }

                String pointName = args[1];
                RespawnPoint point = plugin.getRespawnManager().getRespawnPoint(((Player)sender).getWorld().getName(), pointName);

                if (point == null) {
                    sender.sendMessage(plugin.getMessageManager().getMessage("respawn-point.not-found"));
                    return true;
                }

                ((Player)sender).teleport(point.getLocation());
                sender.sendMessage(plugin.getMessageManager().getMessage("respawn-point.teleported", "%name%", pointName));
                return true;

            case "admin":
                if (sender.hasPermission("pwinggraves.admin")) {
                    plugin.getAdminGUI().openAdminMenu((Player)sender);
                } else {
                    sender.sendMessage(plugin.getMessageManager().getMessage("permission.denied"));
                }
                return true;

            case "menu":
                if (sender.hasPermission("pwinggraves.menu")) {
                    plugin.getRespawnGUI().openMainMenu((Player)sender);
                } else {
                    sender.sendMessage(plugin.getMessageManager().getMessage("permission.denied"));
                }
                return true;

            case "add":
                if (args.length < 2) {
                    sendHelp((Player)sender);
                    return true;
                }
                RespawnPoint newPoint = new RespawnPoint(args[1], ((Player)sender).getLocation());
                plugin.getRespawnManager().addRespawnPoint(((Player)sender).getWorld().getName(), newPoint);
                sender.sendMessage(plugin.getMessageManager().getMessage("respawn-point.added", "%name%", args[1]));
                return true;

            case "remove":
                if (args.length < 2) {
                    sendHelp((Player)sender);
                    return true;
                }
                plugin.getRespawnManager().removeRespawnPoint(((Player)sender).getWorld().getName(), args[1]);
                sender.sendMessage(plugin.getMessageManager().getMessage("respawn-point.removed", "%name%", args[1]));
                return true;

            default:
                sendHelp((Player)sender);
                return true;
        }
    }

    private void sendHelp(Player player) {
        player.sendMessage(plugin.getMessageManager().getMessage("commands.help-header"));
        player.sendMessage(plugin.getMessageManager().getMessage("commands.help-add"));
        player.sendMessage(plugin.getMessageManager().getMessage("commands.help-remove"));
        player.sendMessage(plugin.getMessageManager().getMessage("commands.help-list"));
        player.sendMessage(plugin.getMessageManager().getMessage("commands.help-tp"));
    }
}