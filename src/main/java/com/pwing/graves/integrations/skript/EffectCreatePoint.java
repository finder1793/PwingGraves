package com.pwing.graves.integrations.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.pwing.graves.PwingGraves;
import org.bukkit.Location;
import org.bukkit.event.Event;
import com.pwing.graves.respawn.RespawnPoint;
import com.pwing.graves.PwingGraves;

public class EffectCreatePoint extends Effect {
    private Expression<String> name;
    private Expression<Location> location;

    static {
        Skript.registerEffect(EffectCreatePoint.class, "create [a] respawn point %string% at %location%");
    }
    private static PwingGraves plugin;

    public static void setPlugin(PwingGraves pl) {
        plugin = pl;
    }

    @Override
    protected void execute(Event event) {
        String pointName = name.getSingle(event);
        Location pointLocation = location.getSingle(event);
        if (pointName != null && pointLocation != null) {
            plugin.getRespawnManager().addRespawnPoint(
                pointLocation.getWorld().getName(),
                new RespawnPoint(pointName, pointLocation)
            );
        }
    }
    @Override
    public String toString(Event event, boolean debug) {
        return "create respawn point " + name.toString(event, debug) + " at " + location.toString(event, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        name = (Expression<String>) exprs[0];
        location = (Expression<Location>) exprs[1];
        return true;
    }
}


