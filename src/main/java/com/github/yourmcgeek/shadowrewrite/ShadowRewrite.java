package com.github.yourmcgeek.shadowrewrite;

import com.github.yourmcgeek.shadowrewrite.commands.remind.Add;
import com.github.yourmcgeek.shadowrewrite.commands.remind.Remind;
import com.github.yourmcgeek.shadowrewrite.commands.remind.Remove;
import com.github.yourmcgeek.shadowrewrite.commands.support.LogChannelCommand;
import com.github.yourmcgeek.shadowrewrite.commands.support.SupportCommand;
import com.github.yourmcgeek.shadowrewrite.commands.support.SupportSetup;
import com.github.yourmcgeek.shadowrewrite.commands.wiki.*;
import com.github.yourmcgeek.shadowrewrite.listeners.PrivateMessageListener;
import com.github.yourmcgeek.shadowrewrite.listeners.SuggestionListener;
import com.github.yourmcgeek.shadowrewrite.listeners.SupportCategoryListener;
import com.github.yourmcgeek.shadowrewrite.listeners.TicketChannelsReactionListener;
import com.github.yourmcgeek.shadowrewrite.objects.config.Config;
import com.github.yourmcgeek.shadowrewrite.storage.MySQL;
import com.github.yourmcgeek.shadowrewrite.storage.SQLManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShadowRewrite {

    public SettingsManager mgr = new SettingsManager(Paths.get(".").resolve("conf.json"));
    public JsonArray confirmMessages = new JsonArray();

    private Path logDirectory;
    private Path attachmentDir;
    private Messenger messenger;
    private CommandHandler commandHandler;
    private Path directory;
    private ShadowRewrite bot = this;
    private Logger logger;
    private JDA jda;
    private SQLManager sqlManager;

    public void init(Path directory) throws Exception {
        this.directory = directory;
        logger = LoggerFactory.getLogger("ShadowBot");
        try {
            Config config = mgr.getConfig();
            if (config.getToken().equalsIgnoreCase("changeme")) {
                System.out.println("Please add the bot token into the config.");
                System.exit(1);
            }
            this.jda = new JDABuilder(AccountType.BOT)
                    .setToken(config.getToken())
                    .setEventManager(new ThreadedEventManager())
                    .setGame(Game.playing("play.shadownode.ca"))
                    .build();
            jda.awaitReady();

            logger.info("Setting Preferences...");
            CommandHandler handler = new CommandHandler.Builder(jda)
                .setPrefix(config.getPrefix())
                .setDeleteCommands(true)
                .setGenerateHelp(true)
                .setSendTyping(true)
                .addCustomParameter(bot)
            .build();

            logger.info("Starting Messenger...");
            this.messenger = new Messenger();

            logger.info("Registering Commands...");
            handler.register(new SupportSetup(this));
            handler.register(new SupportCommand(this));
            handler.register(new LogChannelCommand(this));
            handler.register(new LinkAccount(this));
            handler.register(new CrashReport(this));
            handler.register(new Restart(this));
            handler.register(new Claiming(this));
            handler.register(new ChunkLoading(this));
            handler.register(new Wiki(this));
            handler.register(new Relocate(this));
            handler.register(new Crate(this));
//            handler.register(new LMGTFYCommand(this));
            handler.register(new Remind(this));
            handler.register(new Add(this));
            handler.register(new Remove(this));
            handler.register(new com.github.yourmcgeek.shadowrewrite.commands.remind.List(this));

            handler.getCommand(Remind.class).ifPresent(cmd -> cmd.addCustomParam(commandHandler));


            logger.info("Registering Listeners...");
            this.jda.addEventListener(new PrivateMessageListener(this));
            this.jda.addEventListener(new SupportCategoryListener(this));
            this.jda.addEventListener(new TicketChannelsReactionListener(this));
            this.jda.addEventListener(new SuggestionListener(this));

            logger.info("Attempting Connection to Database");
            try {
                this.sqlManager = new SQLManager(this.mgr.getConfig().getHostname(), this.mgr.getConfig().getPort(), this.mgr.getConfig().getDatabaseName(), this.mgr.getConfig().getUsername(), this.mgr.getConfig().getPassword());
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