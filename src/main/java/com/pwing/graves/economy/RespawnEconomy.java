package com.pwing.graves.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class RespawnEconomy {
    private final Economy economy;
    private final double pointCreationCost;
    private final double teleportCost;

    public RespawnEconomy(Economy economy, double pointCreationCost, double teleportCost) {
        this.economy = economy;
        this.pointCreationCost = pointCreationCost;
        this.teleportCost = teleportCost;
    }

    public boolean chargeForPointCreation(Player player) {
        return economy.has(player, pointCreationCost) && economy.withdrawPlayer(player, pointCreationCost).transactionSuccess();
    }

    public boolean chargeForTeleport(Player player) {
        return economy.has(player, teleportCost) && economy.withdrawPlayer(player, teleportCost).transactionSuccess();
    }
}