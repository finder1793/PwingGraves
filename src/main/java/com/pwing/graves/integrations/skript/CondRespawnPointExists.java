package com.pwing.graves.integrations.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import com.pwing.graves.PwingGraves;

public class CondRespawnPointExists extends Condition {
    private Expression<String> name;
    private static PwingGraves plugin;

    static {
        Skript.registerCondition(CondRespawnPointExists.class, "respawn point %string% exists");
    }

    public static void setPlugin(PwingGraves pl) {
        plugin = pl;
    }

    @Override
    public boolean check(Event event) {
        String pointName = name.getSingle(event);
        return pointName != null && plugin.getRespawnManager().pointExists(pointName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        name = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "respawn point " + name.toString(event, debug) + " exists";
    }
}
