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

    private static JDA api;
    private static ShadowRewrite instance;
    private static Config config;

    public static void main(final String[] args) {
        Path p = Paths.get(".").resolve("conf.json");

        SettingsManager sm = new SettingsManager(p);
        setupBot();
    }


    private static void setupBot() {
        try {
            Config config = SettingsManager.getInstance().getConfig();
            CommandClientBuilder builder = new CommandClientBuilder();
            builder.setPrefix(config.getPrefix());
            builder.setGame(Game.playing("play.shadownode.ca"));
            builder.setOwnerId(config.getBotOwnerId());

            builder.addCommands(new SupportSetup());

            CommandClient client = builder.build();
            new JDABuilder(AccountType.BOT)
                    .addEventListener(client)
                    .setToken(config.getToken())
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static JDA getApi() {
        return api;
    }

    public static ShadowRewrite getInstance() {
        return instance;
    }

}