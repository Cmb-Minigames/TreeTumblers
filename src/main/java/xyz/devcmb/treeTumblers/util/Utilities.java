package xyz.devcmb.treeTumblers.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import org.bukkit.OfflinePlayer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Utilities {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static String GetOfflinePlayerName(OfflinePlayer player){
        String URL = "https://api.ashcon.app/mojang/v2/user/" + player.getUniqueId();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                return json.get("username").getAsString();
            }
        } catch (Exception ignored) {
        }

        return null;
    }

    public static Component formatTime(int time){
        int minutes = time / 60;
        int seconds = time % 60;
        return Component.text(String.format("%02d:%02d", minutes, seconds));
    }
}
