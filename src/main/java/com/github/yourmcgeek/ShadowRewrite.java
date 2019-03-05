package com.github.yourmcgeek;

import com.github.yourmcgeek.commands.LMGTFYCommand;
import com.github.yourmcgeek.commands.support.LogChannelCommand;
import com.github.yourmcgeek.commands.support.SupportCommand;
import com.github.yourmcgeek.commands.support.SupportSetup;
import com.github.yourmcgeek.commands.wiki.*;
import com.github.yourmcgeek.listeners.PrivateMessageListener;
import com.github.yourmcgeek.listeners.SuggestionListener;
import com.github.yourmcgeek.listeners.SupportCategoryListener;
import com.github.yourmcgeek.listeners.TicketChannelsReactionListener;
import com.github.yourmcgeek.objects.config.Config;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import me.bhop.bjdautilities.Messenger;
import me.bhop.bjdautilities.command.CommandHandler;
import me.bhop.bjdautilities.command.CommandHandlerBuilder;
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
    public JsonArray remindersJson = new JsonArray();

    private Path logDirectory;
<<<<<<< HEAD
    private Path attachmentDir;
=======
    private Path remindDirectory;
    private Path confirmFile;
    private Path remindersFile;
>>>>>>> Development Branch for Remind Command
    private Messenger messenger;
    private CommandHandlerBuilder handlerBuilder;
    private Path directory;
    private Logger logger;
    private JDA jda;

<<<<<<< HEAD

    public void init(Path directory) throws Exception {
=======
    public static void main(String[] args) {
        Path p1 = Paths.get(".");

        new ShadowRewrite().setupBot(p1);
    }

    private void setupBot(Path directory) {
>>>>>>> Development Branch for Remind Command
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
            CommandHandler handler = new CommandHandlerBuilder(jda)
                .setPrefix(config.getPrefix())
                .setDeleteCommands(true)
                .setGenerateHelp(true)
                .setSendTyping(true)
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
            handler.register(new Tiquality(this));
            handler.register(new ChunkLoading(this));
            handler.register(new Wiki(this));
            handler.register(new Relocate(this));
            handler.register(new Crate(this));
//            handler.register(new LMGTFYCommand(this));

            logger.info("Registering Listeners...");
            this.jda.addEventListener(new PrivateMessageListener(this));
            this.jda.addEventListener(new SupportCategoryListener(this));
            this.jda.addEventListener(new TicketChannelsReactionListener(this));
            this.jda.addEventListener(new SuggestionListener(this));


        } catch (LoginException e) {
            e.printStackTrace();
        }

        try {
<<<<<<< HEAD
            logger.info("Checking Log directories...");
            Path logs = Paths.get(directory + "/logs");
            if (!Files.exists(logs)) {
                Files.createDirectories(logs);
                logDirectory = logs;
=======
            Path path = Paths.get(directory + "/reminders");
            Path file = Paths.get(path + "/confirm.json");
            Path reminder = Paths.get(path + "/ActiveReminders.json");
            if (!path.toFile().exists()) {
                path.toFile().mkdir();
                remindDirectory = path;
>>>>>>> Development Branch for Remind Command
            }
            logDirectory = logs;
            Path attachments = logs.resolve("attachments");
            if (!Files.exists(attachments)) {
                Files.createDirectories(attachments);
                attachmentDir = attachments;
            }
<<<<<<< HEAD
            attachmentDir = attachments;
        } catch (IOException e) {
            logger.error("Error creating directories!", e);
        }
=======
            if (!Files.exists(reminder)) {
                Files.createFile(reminder);
                remindersFile = reminder;
            }
            remindDirectory = path;
            confirmFile = file;
            remindersFile = reminder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadMessages();
        loadTasks();
    }

>>>>>>> Development Branch for Remind Command

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

<<<<<<< HEAD
    public Logger getLogger() {
        return logger;
    }
=======
    public void saveTasks() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (BufferedWriter writer = Files.newBufferedWriter(this.getRemindersFile())) {
            writer.write(gson.toJson(this.getRemindersJson()));
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTasks() {
        try (BufferedReader reader = Files.newBufferedReader(this.getRemindersFile())) {
            JsonParser parser = new JsonParser();
            remindersJson = parser.parse(reader).getAsJsonArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

>>>>>>> Development Branch for Remind Command
    public SettingsManager getSettingsManager() {
        return mgr;
    }

    public JsonArray getConfirmMessages() { return confirmMessages; }

    public JsonArray getRemindersJson() { return remindersJson; }

<<<<<<< HEAD
    public String getGuildId() {
        return mgr.getConfig().getGuildID();
    }
=======
    public Path getConfirmFile() { return confirmFile; }

    public String getGuildId() { return mgr.getConfig().getGuildID(); }
>>>>>>> Development Branch for Remind Command

    public Path getLogDirectory() { return logDirectory; }

<<<<<<< HEAD
=======
    public Path getRemindDirectory() { return remindDirectory; }

    public Path getRemindersFile() {
        return remindersFile;
    }

>>>>>>> Development Branch for Remind Command
    public Messenger getMessenger() {
        return messenger;
    }

    public long getGuildID() {
        return Long.valueOf(mgr.getConfig().getGuildID());
    }

    public Path getAttachmentDir() {
        return attachmentDir;
    }

    public CommandHandlerBuilder getHandlerBuilder() {
        return handlerBuilder;
    }

    private final class ThreadedEventManager extends InterfacedEventManager {
        private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);

        @Override
        public void handle(Event e) {
            executor.submit(() -> super.handle(e));
        }
    }
}