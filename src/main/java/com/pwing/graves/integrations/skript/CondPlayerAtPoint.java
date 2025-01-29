package com.pwing.graves.integrations.skript;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.pwing.graves.PwingGraves;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class CondPlayerAtPoint extends Condition {
    private Expression<Player> player;
    private Expression<String> name;
    private static PwingGraves plugin;

    public static void setPlugin(PwingGraves pl) {
        plugin = pl;
    }

    @Override
    public boolean check(Event event) {
        Player p = player.getSingle(event);
        String pointName = name.getSingle(event);
        return p != null && pointName != null && plugin.getRespawnManager().isPlayerAtRespawnPoint(p, pointName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        name = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "player " + player.toString(event, debug) + " is at respawn point " + name.toString(event, debug);
    }
}
