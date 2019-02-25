package com.github.yourmcgeek;

import com.github.yourmcgeek.commands.remind.*;
import com.github.yourmcgeek.commands.support.*;
import com.github.yourmcgeek.commands.wiki.*;
import com.github.yourmcgeek.listeners.*;
import com.github.yourmcgeek.objects.config.Config;
import com.github.yourmcgeek.objects.message.Messenger;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ShadowRewrite {

    public SettingsManager mgr = new SettingsManager(Paths.get(".").resolve("conf.json"));
    public JsonArray confirmMessages = new JsonArray();

    private Path logDirectory;
    private Path remindDirectory;
    private Path confirmFile;
    private Messenger messenger;
    private Path directory;

    public static void main(String[] args) {
//        Path p = Paths.get(".").resolve("conf.json");
        Path p1 = Paths.get(".");

//        SettingsManager sm = new SettingsManager(p);
        new ShadowRewrite().setupBot(p1);
    }

    private void setupBot(Path directory) {
        this.directory = directory;
        try {
            Config config = mgr.getConfig();
            CommandClientBuilder builder = new CommandClientBuilder();
            builder.setPrefix(config.getPrefix());
            builder.setGame(Game.playing("play.shadownode.ca"));
            builder.setOwnerId(config.getBotOwnerId());

            this.messenger = new Messenger();

            builder.addCommands(
                    new SupportSetup(this),
                    new SupportClose(this),
                    new SupportCommand(this),
                    new LogChannelCommand(this),
                    new LinkAccount(this),
                    new CrashReport(this),
                    new Restart(this),
                    new Claiming(this),
                    new Tiquality(this),
                    new ChunkLoading(this),
                    new Remind(this),
                    new Wiki(this),
                    new Relocate(this),
                    new Crate(this)
            );

            CommandClient client = builder.build();
            new JDABuilder(AccountType.BOT)
                    .addEventListener(client)
                    .addEventListener(new PrivateMessageListener(this))
                    .addEventListener(new SupportCategoryListener(this))
                    .addEventListener(new TicketChannelsReactionListener(this))
                    .addEventListener(new SuggestionListener(this))
                    .addEventListener(new ShutdownListener(this))
                    .addEventListener(new RemindConfirmListener(this))
                    .setToken(config.getToken())
                    .build();

            if (mgr.getConfig().getToken().equalsIgnoreCase("changeme")) {
                System.out.println("Please add the bot token into the config.");
                System.exit(1);
            }
        } catch (LoginException e) {
            e.printStackTrace();
        }
        try {
            Path path = Paths.get(directory + "/logs");
            if (!path.toFile().exists()) {
                path.toFile().mkdir();
                logDirectory = path;
            }
            logDirectory = path;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Path path = Paths.get(directory + "/reminders");
            Path file = Paths.get(directory + "/reminders/confirm.json");
            if (!path.toFile().exists()) {
                path.toFile().mkdir();
                remindDirectory = path;
            }
            if (!Files.exists(file)) {
                Files.createFile(file);
                confirmFile = file;
            }
            remindDirectory = path;
            confirmFile = file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadMessages();
    }


    public List<String[]> getTips() throws IOException, ParseException {
        JsonReader reader = new JsonReader(Files.newBufferedReader(Paths.get(".").resolve("conf.json")));
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse(reader).getAsJsonObject();
        JsonArray tips = result.get("tips").getAsJsonArray();
        List<String[]> tipArray = new ArrayList<>();
        for (JsonElement obj : tips) {
            JsonObject jsonObject = obj.getAsJsonObject();
            String word = jsonObject.get("word").getAsString();
            String suggestion = jsonObject.get("suggestion").getAsString();
            String[] put = new String[]{word, suggestion};
            tipArray.add(put);
        }
        return tipArray;
    }

    public void saveMessages() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (BufferedWriter writer = Files.newBufferedWriter(this.getConfirmFile())) {
            writer.write(gson.toJson(this.getConfirmMessages()));
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMessages() {
        try (BufferedReader reader = Files.newBufferedReader(this.getConfirmFile())) {
            JsonParser parser = new JsonParser();
            confirmMessages = parser.parse(reader).getAsJsonArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SettingsManager getSettingsManager() {
        return mgr;
    }

    public JsonArray getConfirmMessages() {
        return confirmMessages;
    }

    public Path getConfirmFile() { return confirmFile; }

    public String getGuildId() {
        return mgr.getConfig().getGuildID();
    }

    public Path getLogDirectory() {
        return logDirectory;
    }

    public Path getRemindDirectory() { return remindDirectory; }

    public Messenger getMessenger() {
        return messenger;
    }

    public long getGuildID() {
        return Long.valueOf(mgr.getConfig().getGuildID());
    }
}