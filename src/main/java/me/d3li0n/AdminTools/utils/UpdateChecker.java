package me.d3li0n.AdminTools.utils;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {
    private HttpURLConnection connection;
    private String latest = "0.0";
    private String current = "0.0";

    public UpdateChecker(JavaPlugin plugin) {
        try {
            current = plugin.getDescription().getVersion();

            connection = (HttpURLConnection) new URL("https://raw.githubusercontent.com/d3li0n/admintools/master/src/main/resources/plugin.yml").openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            for (String line = reader.readLine(); line != null; line = reader.readLine())
                if (line.contains("version")) { this.latest = line; break; }

            connection.disconnect();
            reader.close();

            this.latest = this.latest.substring(10).replace("\'", "");
        } catch (IOException e) {
            return;
        }
    }

    public boolean isUpdated() {
        String[] latest = this.latest.split("\\.");
        String[] current = this.current.split("\\.");
        int counter = 0;
        for (int i = 0; i < latest.length; i++)
            if (latest[i].equals(current[i])) counter++;
        if (counter == current.length) return true;
        return false;
    }
}
