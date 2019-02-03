package com.github.yourmcgeek;

import com.github.yourmcgeek.objects.config.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SettingsManager {

    private static SettingsManager instance;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Config config;
    private final Path configFile = new File(".").toPath().resolve("config.json");
    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    public SettingsManager() {
        if (!configFile.toFile().exists()) {
            System.out.println("Creating default settings!\nPlease edit the config file with the correct information");
            this.config = getDefaultSettings();
            saveSettings();
            System.exit(0);
        }
        loadSettings();
    }

    private Config getDefaultSettings() {
        Config newConfig = new Config();

        final Config.discord discord = new Config.discord();
        final Config.discord.Game game = new Config.discord.Game();
        game.name = "play.shadownode.ca";
        game.type = 0;
        discord.game = game;
        discord.botOwnerId = "102762443767287808";
        discord.prefix = "?";
        discord.token = "";

        return newConfig;
    }

    private void loadSettings() {
        try {
            checkBadEscapes(configFile);

            BufferedReader reader = Files.newBufferedReader(configFile, StandardCharsets.UTF_8);
            this.config = gson.fromJson(reader, Config.class);
            reader.close();
            System.out.println("Loaded settings!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveSettings() {
        String jsonOut = gson.toJson(this.config);
        try {
            BufferedWriter writer = Files.newBufferedWriter(configFile, StandardCharsets.UTF_8);
            writer.append(jsonOut);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkBadEscapes(Path filePath) throws IOException {
        final byte FORWARD_SLASH = 47;
        final byte BACK_SLASH = 92;

        boolean modified = false;
        byte[] bytes = Files.readAllBytes(filePath);
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == BACK_SLASH) {
                modified = true;
                bytes[i] = FORWARD_SLASH;
            }
        }
        if (modified) {
            Files.write(filePath, bytes);
        }
    }

    public Config getConfig() {
        return config;
    }
}
