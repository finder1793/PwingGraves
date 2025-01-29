package com.pwing.graves.integrations.skript;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.pwing.graves.PwingGraves;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class EffectTeleportToPoint extends Effect {
    private Expression<Player> player;
    private Expression<String> name;
    private static PwingGraves plugin;

    public static void setPlugin(PwingGraves pl) {
        plugin = pl;
    }

    @Override
    protected void execute(Event event) {
        Player p = player.getSingle(event);
        String pointName = name.getSingle(event);
        if (p != null && pointName != null) {
            plugin.getRespawnManager().teleportPlayerToRespawnPoint(p, pointName);
        }
    }

    @Override
    public String toString(Event event, boolean debug) {
        return "teleport player " + player.toString(event, debug) + " to respawn point " + name.toString(event, debug);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        player = (Expression<Player>) exprs[0];
        name = (Expression<String>) exprs[1];
        return true;
    }
}
