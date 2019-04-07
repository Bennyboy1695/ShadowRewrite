package com.github.yourmcgeek.shadowrewrite.listeners;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class CustomChatCommandListener extends ListenerAdapter {

    private ShadowRewrite main;
    private final ScheduledExecutorService executorService;
    final AtomicBoolean hasSent = new AtomicBoolean(false);

    public CustomChatCommandListener() {
        this.executorService = Executors.newScheduledThreadPool(2);
    }

    public void onGuildMessageRecieved(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;

        List<String[]> customChat = new ArrayList<>();
        try {
            customChat = main.getCustomChat();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String[] command : customChat) {
            AtomicLong messageId = new AtomicLong();
            if (event.getMessage().getContentRaw().startsWith(main.getPrefix())) {
                if (hasSent.get())
                    return;
                event.getChannel().sendMessage(new EmbedBuilder()
                        .setColor(new Color(main.getConfig().getConfigValue("customRed").getAsInt(), main.getConfig().getConfigValue("customGreen").getAsInt(), main.getConfig().getConfigValue("customBlue").getAsInt()))
                        .setDescription(command[1]
                                .replaceAll("<tag>", event.getAuthor().getAsMention())
                                .replaceAll("<prefix>", main.getPrefix())
                        ).build()).queue(x -> {
                    messageId.set(x.getIdLong());
                    hasSent.set(true);
                    executorService.schedule(() -> {
                        event.getChannel().getMessageById(messageId.get()).complete().delete().queue();
                        hasSent.set(false);
                        messageId.set(0L);
                    }, 10, TimeUnit.SECONDS);
                });
            }
        }
    }
}