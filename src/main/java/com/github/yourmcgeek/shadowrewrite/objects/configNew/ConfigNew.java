package com.github.yourmcgeek.shadowrewrite.objects.configNew;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ConfigNew {

    private ShadowRewrite main;
    private Path configDirectory;
    private JsonElement conf;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ConfigNew(ShadowRewrite main, Path configDirectory) {
        this.main = main;
        this.configDirectory = configDirectory;
        Path config = configDirectory.resolve("conf.json");
        try {
            if (!Files.exists(config)) {
                Files.createFile(config);
                write(config, writeDefaults());
            }
            if (hasAllEntries(read(config).getAsJsonObject())) {
                this.conf = read(config);
            } else {
                write(config,  writeNotFoundDefaults(read(config).getAsJsonObject()));
                this.conf = read(config);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JsonElement read(Path config) {
        JsonElement element =  null;
        try (BufferedReader reader = Files.newBufferedReader(config)) {
            JsonParser parser = new JsonParser();
            element = parser.parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return element;
    }

    private void write (Path config, JsonObject object) {
        try (BufferedWriter writer = Files.newBufferedWriter(config)) {
            writer.write(gson.toJson(object));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try (BufferedWriter writer = Files.newBufferedWriter(configDirectory.resolve("conf.json"))) {
            writer.write(gson.toJson(conf));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     public JsonObject writeDefaults() {
        JsonObject jo = new JsonObject();
        jo.addProperty("token", "add_me");
        jo.addProperty("commandPrefix", "/");
        jo.addProperty("supportCategoryId", "add_me");
        jo.addProperty("guildID", "add_me");
        jo.addProperty("logChannelId", "add_me");
        jo.addProperty("Red", 0);
        jo.addProperty("Blue", 0);
        jo.addProperty("Green", 0);
        jo.addProperty("linkingURL", "Change this to the proper \"Linking Accounts\" wiki page url");
        jo.addProperty("crashURL", "Change this to the proper \"Crash Reports\" wiki page url");
        jo.addProperty("claimURL", "Change this to the proper \"Claiming\" wiki page url");
        jo.addProperty("clURL", "Change this to the proper \"Chunk Loading\" wiki page url");
        jo.addProperty("restartURL", "Change this to the proper \"Restart and Wipe\" wiki page url");
        jo.addProperty("wikiURL", "Change this to the proper \"Wiki\" direct page url");
        jo.addProperty("crateURL", "Change this to the proper \"Crates\" wiki page url");
        jo.addProperty("relocateURL", "Change this to the proper \"Relocation\" wiki page url");
        jo.addProperty("hostname", "add_me");
        jo.addProperty("port", 3306);
        jo.addProperty("username", "add_me");
        jo.addProperty("password", "add_me");
        jo.addProperty("databaseName", "add_me");
        jo.addProperty("customRed", 0);
        jo.addProperty("customGreen", 0);
        jo.addProperty("customBlue", 0);
        jo.add("customChatCommands", new JsonArray());
        jo.add("channelsToWatch", new JsonArray());
        jo.add("rolesToWatch", new JsonArray());
        jo.add("userMentionToWatch", new JsonArray());
        jo.addProperty("redirectChannelID", 0);
        jo.add("blacklistFiles", new JsonArray());
        jo.add("swearWords", new JsonArray());
        jo.add("tips", new JsonArray());
        return jo;
     }

     public JsonObject writeNotFoundDefaults(JsonObject config) {
        JsonObject finished = new JsonObject();
        for (Map.Entry<String, JsonElement> entrySet : writeDefaults().entrySet()) {
            if (!config.has(entrySet.getKey())) {
                finished.add(entrySet.getKey(), entrySet.getValue());
            } else {
                finished.add(entrySet.getKey(), config.get(entrySet.getKey()));
            }
        }
        return finished;
     }

     public boolean hasAllEntries(JsonObject config) {
        int count = 0;
        for (Map.Entry<String, JsonElement> entrySet : writeDefaults().entrySet()) {
            if (config.has(entrySet.getKey())) {
                count++;
            }
        }
        return (count == writeDefaults().size());
     }

     public <T extends JsonElement> T getConfigValue(String key) {
        JsonObject object = (JsonObject) conf;
        if (object.get(key).isJsonObject()) {
            return (T) object.get(key).getAsJsonObject();
        }
        if (object.get(key).isJsonArray()) {
            return (T) object.get(key).getAsJsonArray();
        }
        return (T) object.get(key);
     }
}