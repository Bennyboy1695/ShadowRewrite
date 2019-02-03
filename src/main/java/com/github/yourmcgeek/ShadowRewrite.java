package com.github.yourmcgeek;

import com.github.yourmcgeek.commands.support.SupportSetup;
import com.github.yourmcgeek.objects.config.Config;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ShadowRewrite {

    private JDA api;
    private ShadowRewrite instance;
    private Config config;
    public SettingsManager mgr = new SettingsManager(Paths.get(".").resolve("conf.json"));

    public static void main(final String[] args) {
        Path p = Paths.get(".").resolve("conf.json");

        SettingsManager sm = new SettingsManager(p);
        new ShadowRewrite().setupBot();
    }


    private void setupBot() {
        try {
            Config config = mgr.getConfig();
            CommandClientBuilder builder = new CommandClientBuilder();
            builder.setPrefix(config.getPrefix());
            builder.setGame(Game.playing("play.shadownode.ca"));
            builder.setOwnerId(config.getBotOwnerId());

            builder.addCommands(new SupportSetup(this));

            CommandClient client = builder.build();
            new JDABuilder(AccountType.BOT)
                    .addEventListener(client)
                    .setToken(config.getToken())
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public JDA getApi() {
        return api;
    }

    public ShadowRewrite getInstance() {
        return instance;
    }

    public SettingsManager getSettingsManager() {
        return mgr;
    }

}