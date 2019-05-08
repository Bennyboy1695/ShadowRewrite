package com.github.yourmcgeek.shadowrewrite;

import com.github.yourmcgeek.shadowrewrite.commands.remind.Add;
import com.github.yourmcgeek.shadowrewrite.commands.remind.Remind;
import com.github.yourmcgeek.shadowrewrite.commands.remind.Remove;
import com.github.yourmcgeek.shadowrewrite.commands.support.*;
import com.github.yourmcgeek.shadowrewrite.commands.wiki.*;
import com.github.yourmcgeek.shadowrewrite.listeners.*;
import com.github.yourmcgeek.shadowrewrite.objects.configNew.ConfigNew;
import com.github.yourmcgeek.shadowrewrite.storage.SQLManager;
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

    public JsonArray confirmMessages = new JsonArray();

    private Path logDirectory;
    private Path attachmentDir;
    private Messenger messenger;
    private CommandHandler commandHandler;
    private Path directory;
    private Path configDirectory;
    private ShadowRewrite bot = this;
    private ConfigNew config;
    private Logger logger;
    private JDA jda;
    private SQLManager sqlManager;

    public void init(Path directory, Path configDirectory) throws Exception {
        this.directory = directory;
        this.configDirectory = configDirectory;
        logger = LoggerFactory.getLogger("ShadowBot");
        logger.info("Initializing Config!");

        try {
            initConfig(configDirectory);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize config!", e);
        }

        try {

            if (config.getConfigValue("token").getAsString().equals("add_me")) {
                System.out.println("Please add the bot token into the config.");
                System.exit(1);
            }

            logger.info("Setting Preferences...");
            this.jda = new JDABuilder(AccountType.BOT)
                    .setToken(config.getConfigValue("token").getAsString())
                    .setEventManager(new ThreadedEventManager())
                    .setGame(Game.playing("play.shadownode.ca"))
                    .build();
            jda.awaitReady();

            logger.info("Starting Messenger...");
            this.messenger = new Messenger();

            logger.info("Registering Commands...");

            CommandHandler handler = new CommandHandler.Builder(jda).setGenerateHelp(true).addCustomParameter(this).setEntriesPerHelpPage(6).guildIndependent().setCommandLifespan(10).setResponseLifespan(10).setPrefix(getPrefix()).build();

            handler.register(new SupportSetup());
            handler.register(new SupportCommand());
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
//            handler.register(new LMGTFYCommand());
            handler.register(new Remind());
            handler.register(new Add());
            handler.register(new Remove());
            handler.register(new com.github.yourmcgeek.shadowrewrite.commands.remind.List());

            handler.getCommand(Remind.class).ifPresent(cmd -> cmd.addCustomParam(handler));


            logger.info("Registering Listeners...");
            this.jda.addEventListener(new CustomChatCommandListener(this));
            this.jda.addEventListener(new PrivateMessageListenerNew(this));
            this.jda.addEventListener(new SupportCategoryListener(this));
            this.jda.addEventListener(new TicketChannelsReactionListener(this));
            this.jda.addEventListener(new SuggestionListener(this));
            this.jda.addEventListener(new TagListener(this));

            logger.info("Attempting Connection to Database");
            try {
                this.sqlManager = new SQLManager(config.getConfigValue("hostname").getAsString(), config.getConfigValue("port").getAsInt(), config.getConfigValue("databaseName").getAsString(), config.getConfigValue("username").getAsString(), config.getConfigValue("password").getAsString());
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
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

    public List<String[]> getTips(){
        JsonArray tips = config.getConfigValue("tips");
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
        JsonArray customChat = config.getConfigValue("customChatCommands");
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

    public void initConfig(Path configDirectory) {
        try {
            config = new ConfigNew(this, configDirectory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JDA getJDA() {
        return this.jda;
    }

    public ConfigNew getConfig() {
        return config;
    }


    public String getPrefix() {
        return config.getConfigValue("commandPrefix").getAsString();
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getLogDirectory() {
        return logDirectory;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public long getGuildID() {
        return config.getConfigValue("guildID").getAsLong();
    }

    public Path getAttachmentDir() {
        return attachmentDir;
    }

    public SQLManager getSqlManager() {
        return sqlManager;
    }

    private final class ThreadedEventManager extends InterfacedEventManager {
        private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);

        @Override
        public void handle(Event e) {
            executor.submit(() -> super.handle(e));
        }
    }
}