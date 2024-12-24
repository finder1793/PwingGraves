package com.pwing.graves.integrations;

import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import com.google.gson.JsonObject;

public class DiscordWebhook {
    private final String webhookUrl;

    public DiscordWebhook(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public void sendPointCreationNotification(String creator, String pointName) {
        JsonObject json = new JsonObject();
        json.addProperty("content", "New respawn point created!");
        json.addProperty("username", "PwingGraves");
        
        JsonObject embed = new JsonObject();
        embed.addProperty("title", "New Respawn Point");
        embed.addProperty("description", "Point '" + pointName + "' created by " + creator);
        
        sendWebhook(json);
    }

    private void sendWebhook(JsonObject json) {
        try {
            URL url = new URL(webhookUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("User-Agent", "PwingGraves");
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            OutputStream stream = connection.getOutputStream();
            stream.write(json.toString().getBytes());
            stream.flush();
            stream.close();

            connection.getInputStream().close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
