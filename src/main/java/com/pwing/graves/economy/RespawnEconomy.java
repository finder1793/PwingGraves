package com.pwing.graves.economy;

import com.pwing.graves.integrations.PwingEcoIntegration;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class RespawnEconomy {
    private final Economy vaultEconomy;
    private final PwingEcoIntegration pwingEcoIntegration;
    private final double pointCreationCost;
    private final double teleportCost;
    private final double respawnCost;
    private final String currencyName;

    public RespawnEconomy(Economy vaultEconomy, PwingEcoIntegration pwingEcoIntegration, double pointCreationCost, double teleportCost, double respawnCost, String currencyName) {
        this.vaultEconomy = vaultEconomy;
        this.pwingEcoIntegration = pwingEcoIntegration;
        this.pointCreationCost = pointCreationCost;
        this.teleportCost = teleportCost;
        this.respawnCost = respawnCost;
        this.currencyName = currencyName;
    }

    public boolean chargeForPointCreation(Player player) {
        return chargePlayer(player, pointCreationCost);
    }

    public boolean chargeForTeleport(Player player) {
        return chargePlayer(player, teleportCost);
    }

    public boolean chargeForRespawn(Player player) {
        return chargePlayer(player, respawnCost);
    }

    private boolean chargePlayer(Player player, double amount) {
        if (pwingEcoIntegration != null) {
            return pwingEcoIntegration.chargePlayer(player, currencyName, amount);
        } else if (vaultEconomy != null) {
            return vaultEconomy.has(player, amount) && vaultEconomy.withdrawPlayer(player, amount).transactionSuccess();
        }
        return false;
    }
}