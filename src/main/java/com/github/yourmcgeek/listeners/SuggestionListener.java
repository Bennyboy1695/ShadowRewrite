/*
 *
 * Original file from NetBans Support Bot. Used with permission, original source here
 * https://github.com/NetBans/SupportBot/
 *
 */

package com.github.yourmcgeek.listeners;

import com.github.yourmcgeek.ShadowRewrite;
import net.dv8tion.jda.core.EmbedBuilder;
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
        if (event.getAuthor().isBot() || event.getMessage().getContentRaw().startsWith(main.mgr.getConfig().getPrefix()))
            return;

        List<String[]> tips = new ArrayList<>();
        try {
            tips = main.getTips();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        for (String[] sugg : tips) {
            AtomicLong messageId = new AtomicLong();
            if (event.getMessage().getContentRaw().toLowerCase().contains(sugg[0])) {
                if (hasSent.get())
                    return;

                event.getChannel().sendMessage(new EmbedBuilder()
                        .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                        .setDescription(sugg[1]
                                .replaceAll("<tag>", event.getAuthor().getAsMention())
                                .replaceAll("<prefix>", main.mgr.getConfig().getPrefix())
                                .replaceAll("<forum>", "https://shadownode.ca")
                                .replaceAll("<wiki>", "https://shadownode.ca/wiki")
                                .replaceAll("<support>", "If you need support, create a new ticket with by running " +  main.mgr.getConfig().getPrefix() + "ticket")
                        ).build()).queue(x -> {
                    messageId.set(x.getIdLong());
                    hasSent.set(true);
                    executorService.schedule(()-> {
                        event.getChannel().getMessageById(messageId.get()).complete().delete().complete();
                        hasSent.set(false);
                        messageId.set(0L);
                    },10, TimeUnit.SECONDS);
                });
            }
        }
    }
}