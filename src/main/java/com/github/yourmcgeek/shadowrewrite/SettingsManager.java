package com.github.yourmcgeek.shadowrewrite;

import com.github.yourmcgeek.shadowrewrite.objects.config.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SettingsManager {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private Config loadedConf;
    private final Path confPath;

    public SettingsManager (Path conf) {
        this.confPath = conf;
        try {
            if (!Files.exists(conf)) {
                try (BufferedWriter writer = Files.newBufferedWriter(conf, StandardCharsets.UTF_8)) {
                    loadedConf = new Config();
                    gson.toJson(loadedConf, writer);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loadConfig();
    }

    public void loadConfig() {
        Config loaded = null;
        try (BufferedReader reader = Files.newBufferedReader(confPath, StandardCharsets.UTF_8)) {
            loaded = gson.fromJson(reader, Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (loaded == null)
            loaded = new Config();
        this.loadedConf = loaded;
    }

    public Config getConfig() {
        return this.loadedConf;
    }

}