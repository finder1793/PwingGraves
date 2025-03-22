package com.pwing.graves.integrations;

import com.pwing.pwingeco.PwingEco;
import com.pwing.pwingeco.api.ShopIntegrationAPI;
import org.bukkit.entity.Player;

public class PwingEcoIntegration {
    private final ShopIntegrationAPI shopAPI;

    public PwingEcoIntegration(PwingEco pwingEco) {
        this.shopAPI = new ShopIntegrationAPI(pwingEco);
    }

    public boolean chargePlayer(Player player, String currencyName, double amount) {
        return shopAPI.processPurchase(player, currencyName, amount);
    }

    public boolean rewardPlayer(Player player, String currencyName, double amount) {
        return shopAPI.processSale(player, currencyName, amount);
    }

    public double getPlayerBalance(Player player, String currencyName) {
        return shopAPI.getCurrencyBalance(player.getUniqueId(), currencyName);
    }
}
