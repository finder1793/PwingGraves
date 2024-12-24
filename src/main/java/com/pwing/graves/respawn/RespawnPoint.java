package com.pwing.graves.respawn;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import java.util.HashMap;
import java.util.Map;

public class RespawnPoint implements ConfigurationSerializable {
    private final Location location;
    private final String name;

    public RespawnPoint(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("location", location);
        return map;
    }

    public static RespawnPoint deserialize(Map<String, Object> map) {
        return new RespawnPoint((String) map.get("name"), (Location) map.get("location"));
    }
}
