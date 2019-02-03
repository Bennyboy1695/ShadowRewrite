package com.github.yourmcgeek;

import com.github.yourmcgeek.commands.support.SupportSetup;
import com.github.yourmcgeek.objects.config.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


public class ShadowRewrite {

    private static JDA api;
    private static ShadowRewrite instance;

    public static void main(final String[] args) {
        Config config = new Config();
        Path file = new File(".").toPath().resolve("config.json");
        if (!file.toFile().exists()) {
            try {
                BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                writer.write(gson.toJson(config));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setupBot();
    }

    private static void setupBot() {
        try {
            Config config = Variables.getInstance().getConfig();
            CommandClientBuilder ccBuild = new CommandClientBuilder();
            ccBuild.setPrefix(config.getPrefix());
            ccBuild.setGame(Game.playing("play.shadownode.ca"));
            ccBuild.setOwnerId(config.getBotOwnerId());

            ccBuild.addCommands(new SupportSetup());

            CommandClient client = ccBuild.build();
            new JDABuilder(AccountType.BOT)
                    .addEventListener(client)
                    .setToken(config.getToken())
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
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