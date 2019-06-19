package com.github.yourmcgeek.shadowrewrite;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Config {

    private ShadowRewrite main;
    private Path configDirectory;
    private JsonElement conf;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Config(ShadowRewrite main, Path configDirectory) {
        this.main = main;
        this.configDirectory = configDirectory;
    }

    JsonObject newConfig(String configName, JsonObject configOptions) {
        JsonObject conf = null;
        Path config = configDirectory.resolve(configName + ".json");
        try {
            if (!Files.exists(config)) {
                Files.createFile(config);
                write(config, configOptions);
            }
            if (hasAllEntries(read(config).getAsJsonObject(), configOptions)) {
                conf = read(config).getAsJsonObject();
            } else {
                write(config, writeNotFoundDefaults(read(config).getAsJsonObject(), configOptions));
                conf = read(config).getAsJsonObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.conf = conf;
        return conf;
    }

    private JsonElement read(Path config) {
        JsonElement element = null;
        try (BufferedReader reader = Files.newBufferedReader(config)) {
            JsonParser parser = new JsonParser();
            element = parser.parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return element;
    }

    private void write(Path config, JsonObject object) {
        try (BufferedWriter writer = Files.newBufferedWriter(config)) {
            writer.write(gson.toJson(object));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig(String configName, JsonElement conf) {
        try (BufferedWriter writer = Files.newBufferedWriter(configDirectory.resolve(configName + ".json"))) {
            writer.write(gson.toJson(conf));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     public static JsonObject writeDefaults() {
        JsonObject jo = new JsonObject();
        jo.addProperty("token", "add_me");
        jo.addProperty("commandPrefix", "/");
        jo.addProperty("supportCategoryId", "add_me");
        jo.addProperty("guildID", "add_me");
        jo.addProperty("logChannelId", "add_me");
        jo.addProperty("TicketCreationChannelID", "add_me");
        JsonObject redisSettings = new JsonObject();
        redisSettings.addProperty("hostname", "localhost");
        redisSettings.addProperty("port", 6379);
        redisSettings.addProperty("database", 0);
        redisSettings.addProperty("password", "password");
        redisSettings.addProperty("poolname", "master");
        redisSettings.add("sentinels", new JsonArray());
        jo.add("redis", redisSettings);
        jo.addProperty("Red", 0);
        jo.addProperty("Blue", 0);
        jo.addProperty("Green", 0);
        jo.addProperty("linkingURL", "Change this to the proper \"Linking Accounts\" wiki page url");
        jo.addProperty("crashURL", "Change this to the proper \"Crash Reports\" wiki page url");
        jo.addProperty("claimURL", "Change this to the proper \"Claiming\" wiki page url");
        jo.addProperty("chunkURL", "Change this to the proper \"Chunk Loading\" wiki page url");
        jo.addProperty("restartURL", "Change this to the proper \"Restart and Wipe\" wiki page url");
        jo.addProperty("wikiURL", "Change this to the proper \"Wiki\" direct page url");
        jo.addProperty("crateURL", "Change this to the proper \"Crates\" wiki page url");
        jo.addProperty("relocateURL", "Change this to the proper \"Relocation\" wiki page url");
        jo.add("swearWords", new JsonArray());
        jo.add("tips", new JsonArray());
        return jo;
     }

    public JsonObject writeNotFoundDefaults(JsonObject config, JsonObject configOptions) {
        JsonObject finished = new JsonObject();
        for (Map.Entry<String, JsonElement> entrySet : configOptions.entrySet()) {
            if (!config.has(entrySet.getKey())) {
                finished.add(entrySet.getKey(), entrySet.getValue());
            } else {
                finished.add(entrySet.getKey(), config.get(entrySet.getKey()));
            }
        }
        return finished;
    }

    public boolean hasAllEntries(JsonObject config, JsonObject configOptions) {
        int count = 0;
        for (Map.Entry<String, JsonElement> entrySet : configOptions.entrySet()) {
            if (config.has(entrySet.getKey())) {
                count++;
            }
        }
        return (count == configOptions.size());
    }

    public <T extends JsonElement> T getConfigValue(String... keys) {
        JsonObject parent = (JsonObject) conf;
        JsonElement temp = parent.get(keys[0]);
        if (temp.isJsonArray())
            return (T) temp.getAsJsonArray();
        JsonElement object = temp;
        try {
            for (int i = 1; i < keys.length; i++) {
                temp = ((JsonObject)object).get(keys[i]);
                if (temp.isJsonArray())
                    return (T) temp.getAsJsonArray();
                if (temp.isJsonPrimitive())
                    return (T) temp.getAsJsonPrimitive();
                object = temp.getAsJsonObject();
            }
        } catch (NullPointerException e) {
            return (T) object;
        }
        return (T) object;
    }
}