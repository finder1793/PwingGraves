package com.pwing.graves.integrations.skript;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.pwing.graves.PwingGraves;
import com.pwing.graves.respawn.RespawnPoint;
import org.bukkit.event.Event;

public class EffectRemovePoint extends Effect {
    private Expression<String> name;
    private static PwingGraves plugin;

    public static void setPlugin(PwingGraves pl) {
        plugin = pl;
    }

    @Override
    protected void execute(Event event) {
        String pointName = name.getSingle(event);
        if (pointName != null) {
            // Retrieve the RespawnPoint object
            for (String worldName : plugin.getRespawnManager().getWorldNames()) {
                RespawnPoint point = plugin.getRespawnManager().getRespawnPoint(worldName, pointName);
                if (point != null) {
                    plugin.getRespawnManager().removeRespawnPoint(worldName, pointName);
                    break;
                }
            }
        }
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "remove respawn point " + name.toString(event, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        name = (Expression<String>) exprs[0];
        return true;
    }
}
