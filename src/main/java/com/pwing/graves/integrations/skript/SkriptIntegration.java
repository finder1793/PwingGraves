package com.pwing.graves.integrations.skript;

import ch.njol.skript.Skript;

public class SkriptIntegration {

    public static void registerEffects() {
        Skript.registerEffect(EffectCreatePoint.class, "create [a] respawn point %string% at %location%");
        Skript.registerCondition(CondRespawnPointExists.class, "respawn point %string% exists");
        Skript.registerEffect(EffectRemovePoint.class, "remove respawn point %string%");
        Skript.registerCondition(CondPlayerAtPoint.class, "player %player% is at respawn point %string%");
        Skript.registerEffect(EffectTeleportToPoint.class, "teleport player %player% to respawn point %string%");
    }
}
