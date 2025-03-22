package com.pwing.graves.economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class RespawnEconomy {
    private final Economy economy;
    private final double pointCreationCost;
    private final double teleportCost;
    private final double respawnCost;

    public RespawnEconomy(Economy economy, double pointCreationCost, double teleportCost, double respawnCost) {
        this.economy = economy;
        this.pointCreationCost = pointCreationCost;
        this.teleportCost = teleportCost;
        this.respawnCost = respawnCost;
    }

    public boolean chargeForPointCreation(Player player) {
        return economy.has(player, pointCreationCost) && economy.withdrawPlayer(player, pointCreationCost).transactionSuccess();
    }

    public boolean chargeForTeleport(Player player) {
        return economy.has(player, teleportCost) && economy.withdrawPlayer(player, teleportCost).transactionSuccess();
    }

    public boolean chargeForRespawn(Player player) {
        if (!economy.has(player, respawnCost)) {
            player.sendMessage("You do not have enough money to respawn!"); // Replace with a message from the MessageManager
            return false;
        }
        return economy.withdrawPlayer(player, respawnCost).transactionSuccess();
    }
}