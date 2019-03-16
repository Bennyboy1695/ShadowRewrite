package com.github.yourmcgeek;

import com.github.yourmcgeek.commands.LMGTFYCommand;
import com.github.yourmcgeek.commands.support.LogChannelCommand;
import com.github.yourmcgeek.commands.support.SupportCommand;
import com.github.yourmcgeek.commands.support.SupportSetup;
import com.github.yourmcgeek.commands.wiki.*;
import com.github.yourmcgeek.listeners.*;
import com.github.yourmcgeek.objects.config.Config;
import com.github.yourmcgeek.objects.message.Messenger;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.hooks.InterfacedEventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShadowRewrite {

    public SettingsManager mgr = new SettingsManager(Paths.get(".").resolve("conf.json"));
    public JsonArray confirmMessages = new JsonArray();

    private Path logDirectory;
    private Messenger messenger;
    private Path directory;
    private Logger logger;
    private JDA jda;


    public void setupBot(Path directory) throws Exception {
        this.directory = directory;
        logger = LoggerFactory.getLogger("ShadowBot");
        try {
            Config config = mgr.getConfig();
            CommandClientBuilder builder = new CommandClientBuilder();
            builder.setPrefix(config.getPrefix());
            builder.setGame(Game.playing("play.shadownode.ca"));
            builder.setOwnerId(config.getBotOwnerId());

            this.messenger = new Messenger();

            builder.addCommands(
                    new SupportSetup(this),
                    new SupportCommand(this),
                    new LogChannelCommand(this),
                    new LinkAccount(this),
                    new CrashReport(this),
                    new Restart(this),
                    new Claiming(this),
                    new Tiquality(this),
                    new ChunkLoading(this),
                    new Wiki(this),
                    new Relocate(this),
                    new Crate(this),
                    new LMGTFYCommand(this)
            );

            CommandClient client = builder.build();
            new JDABuilder(AccountType.BOT)
                    .addEventListener(client)
                    .addEventListener(new PrivateMessageListener(this))
                    .addEventListener(new SupportCategoryListener(this))
                    .addEventListener(new TicketChannelsReactionListener(this))
                    .addEventListener(new SuggestionListener(this))
                    .setEventManager(new ThreadedEventManager())
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
        logger.info("Ready to accept input!");
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

    public void shutdown() {
        logger.info("Initiating Shutdown...");
        getJDA().shutdown();
        logger.info("Shutdown Complete.");
    }


    public JDA getJDA() {
        return this.jda;
    }

    public Logger getLogger() {
        return logger;
    }
    public SettingsManager getSettingsManager() {
        return mgr;
    }

    public JsonArray getConfirmMessages() {
        return confirmMessages;
    }

    public String getGuildId() {
        return mgr.getConfig().getGuildID();
    }

    public Path getLogDirectory() {
        return logDirectory;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public long getGuildID() {
        return Long.valueOf(mgr.getConfig().getGuildID());
    }

    private final class ThreadedEventManager extends InterfacedEventManager {
        private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);

        @Override
        public void handle(Event e) {
            executor.submit(() -> super.handle(e));
        }
    }
}