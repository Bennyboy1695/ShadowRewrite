package com.github.yourmcgeek.shadowrewrite.listeners.discord;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class SuggestionListener extends ListenerAdapter {

    private ShadowRewrite main;
    private final ScheduledExecutorService executorService;
    final AtomicBoolean hasSent = new AtomicBoolean(false);
    public SuggestionListener(ShadowRewrite main) {
        this.main = main;
        this.executorService = Executors.newScheduledThreadPool(2);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getMessage().getContentRaw().startsWith(main.getPrefix()))
            return;

        List<String[]> tips = new ArrayList<>();
        try {
            tips = main.getTips();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String[] sugg : tips) {
            AtomicLong messageId = new AtomicLong();
            if (event.getMessage().getContentRaw().toLowerCase().contains(sugg[0])) {
                if (hasSent.get())
                    return;

                event.getChannel().sendMessage(new EmbedBuilder()
                        .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()))
                        .setDescription(sugg[1]
                                .replaceAll("<tag>", event.getAuthor().getAsMention())
                                .replaceAll("<prefix>", main.getPrefix())
                                .replaceAll("<forum>", "https://shadownode.ca")
                                .replaceAll("<wiki>", "https://shadownode.ca/wiki")
                                .replaceAll("<support>", "If you need support, create a new ticket by running " +  main.getPrefix() + "ticket")
                        ).build()).queue(x -> {
                    messageId.set(x.getIdLong());
                    hasSent.set(true);
                    executorService.schedule(()-> {
                        event.getChannel().getMessageById(messageId.get()).complete().delete().queue();
                        hasSent.set(false);
                        messageId.set(0L);
                    },10, TimeUnit.SECONDS);
                });
            }
        }

        if (event.getMessage().getMentionedRoles().stream().map(Role::getName).anyMatch("Staff"::equalsIgnoreCase)) {

        }
    }
}