package com.pwing.graves.integrations;

import com.pwing.graves.PwingGraves;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class GravesPlaceholderExpansion extends PlaceholderExpansion {
    private final PwingGraves plugin;

    public GravesPlaceholderExpansion(PwingGraves plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "pwinggraves";
    }

    @Override
    public String getAuthor() {
        return "finder17";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) return "";

        return switch (identifier) {
            case "total_points" -> String.valueOf(plugin.getRespawnManager().getTotalPoints());
            case "nearest_point" -> plugin.getRespawnManager().getNearestPointName(player.getLocation());
            case "points_in_world" -> String.valueOf(plugin.getRespawnManager().getPointsInWorld(player.getWorld().getName()).size());
            default -> null;
        };
    }
}
