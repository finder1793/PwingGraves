package com.pwing.graves.api;

import com.pwing.graves.respawn.RespawnPoint;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface PwingGravesAPI {
    RespawnPoint createRespawnPoint(Player creator, Location location, String name);
    RespawnPoint createTemporaryPoint(Player creator, Location location, String name, long duration, int maxUses);
    void removeRespawnPoint(World world, String name);
    Location getNearestRespawnPoint(Location location);
    boolean isRespawnPointValid(RespawnPoint point);
}
