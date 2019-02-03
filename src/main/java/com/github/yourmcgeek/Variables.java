package com.github.yourmcgeek;

import com.github.yourmcgeek.objects.config.Config;
import com.google.common.io.Files;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class Variables {

    private static Variables instance;
    private Config config;
    private final String googleBaseUrl;

    private Variables() {
        try {
            final String json = Files.asCharSource(new File("config.json"), StandardCharsets.UTF_8).read();
            this.config = new Gson().fromJson(json, Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        } if (this.config == null) {
            System.exit(0);
        }
        this.googleBaseUrl = "https:///www.googleapis.com/customsearch/v1?q%s&cx=012048784535646064391:v-fxkttbw54" +
                "&h1=en&searchType=image&key=" + this.config.apis.google + "&safe=off";
    }

    public Config getConfig() {
        return config;
    }

    public String getGoogleBaseUrl() {
        return this.googleBaseUrl;
    }

    public static synchronized Variables getInstance() {
        if (instance == null) {
            instance = new Variables();
        }
        return instance;
    }

}
