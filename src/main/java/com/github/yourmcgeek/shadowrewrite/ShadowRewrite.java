package com.github.yourmcgeek.shadowrewrite;

import com.github.yourmcgeek.shadowrewrite.commands.EmbedCommand;
import com.github.yourmcgeek.shadowrewrite.commands.support.LogChannelCommand;
import com.github.yourmcgeek.shadowrewrite.commands.support.ServerCommand;
import com.github.yourmcgeek.shadowrewrite.commands.support.SupportSetup;
import com.github.yourmcgeek.shadowrewrite.commands.support.UsernameCommand;
import com.github.yourmcgeek.shadowrewrite.commands.wiki.*;
import com.github.yourmcgeek.shadowrewrite.listeners.discord.SuggestionListener;
import com.github.yourmcgeek.shadowrewrite.listeners.discord.SupportCategoryListener;
import com.github.yourmcgeek.shadowrewrite.listeners.discord.TicketChannelsReactionListener;
import com.github.yourmcgeek.shadowrewrite.listeners.discord.TicketCreationListener;
import com.github.yourmcgeek.shadowrewrite.listeners.redis.RedisClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.bhop.bjdautilities.Messenger;
import me.bhop.bjdautilities.command.CommandHandler;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShadowRewrite {

    private Path logDirectory;
    private Path attachmentDir;
    private Messenger messenger;
    private Path directory;
    private Path configDirectory;
    private ShadowRewrite bot = this;
    private Config mainConfig;
    private JsonObject mainConf;
    private Logger logger;
    private JDA jda;
    private RedisClient redisClient;

    public void init(Path directory, Path configDirectory) throws Exception {
        this.directory = directory;
        this.configDirectory = configDirectory;
        logger = LoggerFactory.getLogger("ShadowBot");
        logger.info("Initializing Config!");

        try {
            initConfig(configDirectory);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize mainConfig!", e);
        }

        //redisClient = new RedisClient(this);
        //if (redisClient.load()) {
        //    new TicketsListener(this).register();
        //}

        try {

            if (mainConfig.getConfigValue("token").getAsString().equals("add_me")) {
                System.out.println("Please add the bot token into the Config.");
                System.exit(1);
            }

            logger.info("Setting Preferences...");
            this.jda = new JDABuilder(AccountType.BOT)
                    .setToken(mainConfig.getConfigValue("token").getAsString())
                    .setEventManager(new ThreadedEventManager())
                    .setGame(Game.playing("play.shadownode.ca"))
                    .build();
            jda.awaitReady();

            logger.info("Starting Messenger...");
            this.messenger = new Messenger();

            logger.info("Registering Commands...");

            CommandHandler handler = new CommandHandler.Builder(jda).setGenerateHelp(true).addCustomParameter(this).setEntriesPerHelpPage(6).guildIndependent().setCommandLifespan(10).setResponseLifespan(10).setPrefix(getPrefix()).build();

            handler.register(new SupportSetup());
            handler.register(new LogChannelCommand());
            handler.register(new LinkAccount());
            handler.register(new CrashReport());
            handler.register(new Restart());
            handler.register(new Claiming());
            handler.register(new ChunkLoading());
            handler.register(new Wiki());
            handler.register(new Relocate());
            handler.register(new Crate());
            handler.register(new UsernameCommand());
            handler.register(new ServerCommand());
            handler.register(new EmbedCommand());

            logger.info("Registering Listeners...");
            this.jda.addEventListener(new TicketCreationListener(this));
            this.jda.addEventListener(new SupportCategoryListener(this));
            this.jda.addEventListener(new TicketChannelsReactionListener(this));
            this.jda.addEventListener(new SuggestionListener(this));

        } catch (LoginException e) {
            e.printStackTrace();
        }

        try {
            logger.info("Checking Log directories...");
            Path logs = Paths.get(directory + "/logs");
            if (!Files.exists(logs)) {
                Files.createDirectories(logs);
                logDirectory = logs;
            }
            logDirectory = logs;
            Path attachments = logs.resolve("attachments");
            if (!Files.exists(attachments)) {
                Files.createDirectories(attachments);
                attachmentDir = attachments;
            }
            attachmentDir = attachments;
        } catch (IOException e) {
            logger.error("Error creating directories!", e);
        }

        logger.info("Everything Loaded Successfully | Ready to accept input!");
    }

    public List<String[]> getTips() {
        JsonArray tips = mainConfig.getConfigValue("tips");
        List<String[]> tipArray = new ArrayList<>();
        for (Object obj : tips) {
            JsonObject jsonObject = (JsonObject) obj;
            String word = jsonObject.get("word").getAsString();
            String suggestion = jsonObject.get("suggestion").getAsString();
            String[] put = new String[]{word, suggestion};
            tipArray.add(put);
        }
        return tipArray;
    }

    public List<String[]> getCustomChat() {
        JsonArray customChat = mainConfig.getConfigValue("customChatCommands");
        List<String[]> customChatArray = new ArrayList<>();
        for (Object obj : customChat) {
            JsonObject jsonObject = (JsonObject) obj;
            String command = jsonObject.get("command").getAsString();
            String result = jsonObject.get("result").getAsString();
            String[] put = new String[]{command, result};
            customChatArray.add(put);
        }
        return customChatArray;
    }

    public void shutdown() {
        logger.info("Initiating Shutdown...");
        getJDA().shutdown();
        logger.info("Shutdown Complete.");
    }

    private void initConfig(Path configDirectory){
        try {
            mainConfig = new Config(this, configDirectory);
            mainConf = mainConfig.newConfig("config", Config.writeDefaults());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JDA getJDA() {
        return this.jda;
    }

    public Config getMainConfig() {
        return mainConfig;
    }

    public String getPrefix() {
        return mainConfig.getConfigValue("commandPrefix").getAsString();
    }

    public Logger getLogger() {
        return logger;
    }

    public RedisClient getClient() {
        return redisClient;
    }

    public Path getLogDirectory() {
        return logDirectory;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public long getGuildID() {
        return mainConfig.getConfigValue("guildID").getAsLong();
    }

    public Path getAttachmentDir() {
        return attachmentDir;
    }

    private final class ThreadedEventManager extends InterfacedEventManager {
        private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

        @Override
        public void handle(Event e) {
            executor.submit(() -> super.handle(e));
        }
    }
}