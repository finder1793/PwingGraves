package com.pwing.graves.integrations;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardHook {
    private final WorldGuard worldGuard;

    public WorldGuardHook() {
        this.worldGuard = WorldGuard.getInstance();
    }

    public boolean canCreatePointAtLocation(Location location, Player player) {
        RegionContainer container = worldGuard.getPlatform().getRegionContainer();
        return container.createQuery().testState(
            BukkitAdapter.adapt(location),
            WorldGuardPlugin.inst().wrapPlayer(player),
            Flags.BUILD
        );
    }
}