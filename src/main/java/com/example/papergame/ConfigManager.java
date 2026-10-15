package com.example.papergame;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "papergame.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static ConfigData data = new ConfigData();

    public static class ConfigData {
        public boolean rigEnabled = false;
        public String blockA = "minecraft:diamond_block";
        public String blockE = "minecraft:redstone_block";
        public String activeSide = "A";
        public double simulationBalance = 1000.0;
        public int totalWins = 0;
        public int totalLosses = 0;
    }

    public static void loadConfig() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                ConfigData loaded = GSON.fromJson(reader, ConfigData.class);
                if (loaded != null) {
                    data = loaded;
                }
            } catch (IOException e) {
                PaperGameMod.LOGGER.error("Failed to load PaperGame configuration", e);
            }
        } else {
            saveConfig();
        }
    }

    public static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(data, writer);
        } catch (IOException e) {
            PaperGameMod.LOGGER.error("Failed to save PaperGame configuration", e);
        }
    }
}
