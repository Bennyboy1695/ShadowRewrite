package com.github.yourmcgeek;

import com.github.yourmcgeek.commands.restricted.EvalCommand;
import com.github.yourmcgeek.commands.support.LogChannelCommand;
import com.github.yourmcgeek.commands.support.SupportClose;
import com.github.yourmcgeek.commands.support.SupportCommand;
import com.github.yourmcgeek.commands.support.SupportSetup;
import com.github.yourmcgeek.listeners.PrivateMessageListener;
import com.github.yourmcgeek.listeners.SupportCategoryListener;
import com.github.yourmcgeek.listeners.TicketChannelsReactionListener;
import com.github.yourmcgeek.objects.config.Config;
import com.github.yourmcgeek.objects.message.Messenger;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ShadowRewrite {


    public SettingsManager mgr = new SettingsManager(Paths.get(".").resolve("conf.json"));
    private Path logDirectory;
    private Messenger messenger;
    private Path directory;
    public static void main(final String[] args) {
        Path p = Paths.get(".").resolve("conf.json");
        Path p1 = Paths.get(".");

        SettingsManager sm = new SettingsManager(p);
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

            System.out.println("Loading Messenger...");
            this.messenger = new Messenger();

            builder.addCommands(
                    new SupportSetup(this),
                    new SupportClose(this),
                    new SupportCommand(this),
                    new LogChannelCommand(this),
                    new EvalCommand(this)
            );

            CommandClient client = builder.build();
            new JDABuilder(AccountType.BOT)
                    .addEventListener(client)
                    .addEventListener(new PrivateMessageListener(this))
                    .addEventListener(new SupportCategoryListener(this))
                    .addEventListener(new TicketChannelsReactionListener(this))
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
    }

    public SettingsManager getSettingsManager() {
        return mgr;
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
}