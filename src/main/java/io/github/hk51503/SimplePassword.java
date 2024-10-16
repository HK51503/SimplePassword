package io.github.hk51503;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public final class SimplePassword extends JavaPlugin {
    private Set<String> authenticatedPlayers;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        loadAuthenticatedPlayers();
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveAuthenticatedPlayers();
    }

    private void loadAuthenticatedPlayers() {
        authenticatedPlayers = new HashSet<>();
        File file = new File(getDataFolder(), "authenticated_players.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                authenticatedPlayers.add(line.trim());
            }
        } catch (IOException e) {
            getLogger().warning("Could not load authenticated players: " + e.getMessage());
        }
    }

    private void saveAuthenticatedPlayers() {
        File file = new File(getDataFolder(), "authenticated_players.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String player : authenticatedPlayers) {
                writer.write(player);
                writer.newLine();
            }
        } catch (IOException e) {
            getLogger().warning("Could not save authenticated players: " + e.getMessage());
        }
    }

    public Set<String> getAuthenticatedPlayers() {
        return authenticatedPlayers;
    }

    public void addAuthenticatedPlayer(String playerName) {
        authenticatedPlayers.add(playerName);
    }
}
