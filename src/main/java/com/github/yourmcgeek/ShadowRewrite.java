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

public class ShadowRewrite {

    private static JDA api;
    private static ShadowRewrite instance;

    public static void main(final String[] args) {
        System.out.println("mainDone");
        setupBot();
    }

    private static void setupBot() {
        try {
            Config config = SettingsManager.getInstance().getConfig();
            CommandClientBuilder ccBuild = new CommandClientBuilder();
            ccBuild.setPrefix(config.discord.prefix);
            ccBuild.setGame(Game.of(Game.GameType.fromKey(config.discord.game.type), config.discord.game.name, config.discord.game.streamUrl));
            ccBuild.setOwnerId(config.discord.botOwnerId);

            ccBuild.addCommands(new SupportSetup());

            CommandClient client = ccBuild.build();
            new JDABuilder(AccountType.BOT)
                    .addEventListener(client)
                    .setToken(config.discord.token)
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
