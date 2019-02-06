package com.github.yourmcgeek.listeners;

import com.github.yourmcgeek.ShadowRewrite;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class SupportCategoryListener extends ListenerAdapter {

    private ShadowRewrite main;

    public SupportCategoryListener(ShadowRewrite main) {
        this.main = main;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("[dd/MM/YY HH:mm]");
        if (event.getChannel().getParent().getIdLong() == Long.valueOf(main.mgr.getConfig().getSupportCategoryId())) {
            if (event.getChannel().getIdLong() != Long.valueOf(main.mgr.getConfig().getLogChannelID())) {
                try {
                    if (!Files.exists(main.getLogDirectory().resolve(event.getChannel().getName() + ".log"))) {
                        Files.createFile(main.getLogDirectory().resolve(event.getChannel().getName() + ".log"));
                    }
                    String content = "";
                    if (!event.getMessage().getMentionedMembers().isEmpty()) {
                        String message = event.getMessage().getContentDisplay();
                        content = ("[" + OffsetDateTime.now().format(format) + "] " + event.getMember().getEffectiveName() + ": " +  message);
                    } else {
                        content = ("[" + OffsetDateTime.now().format(format) + "] " + event.getMember().getEffectiveName() + ": " + event.getMessage().getContentRaw());
                    }
                    Files.write(main.getLogDirectory().resolve(event.getChannel().getName() + ".log"), (content + "\n").getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}