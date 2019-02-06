package com.github.yourmcgeek.listeners;

import com.github.yourmcgeek.ShadowRewrite;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class TicketChannelsReactionListener extends ListenerAdapter {

private ShadowRewrite main;

public TicketChannelsReactionListener(ShadowRewrite main) {
    this.main = main;
}

@Override
public void onMessageReactionAdd(MessageReactionAddEvent event) {
    if (event.isFromType(ChannelType.TEXT)) {
        TextChannel channel = (TextChannel) event.getChannel();
        if (channel.getParent().getIdLong() == Long.valueOf(main.mgr.getConfig().getSupportCategoryId())) {
            for (Message message : channel.getPinnedMessages().complete()) {
                if (message.getAuthor().isBot() && event.getReactionEmote().getName().equals("\u2705")) {
                    String cTopicFull = ((TextChannel) event.getChannel()).getTopic();
                    String[] cTopicSplit = cTopicFull.split(" ");
                    String topicID = cTopicSplit[0];
                    if (event.getMember().getUser().getIdLong() == Long.valueOf(topicID) || event.getMember().getRoles().contains(event.getGuild().getRolesByName("Staff", true)) ||
                            event.getMember().getRoles().contains(event.getGuild().getRolesByName("Developer", true))) {
                        event.getJDA().getUserById(Long.valueOf(topicID)).openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                                .setTitle("Issue Completed")
                                .setDescription("Because of this we have sent you a log file containing the history, so that you may look at it in case you encounter the issue again!")
                                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                                .addField("Next Step: ", "If the issue still persists, please create a new ticket!", false)
                                .build()).complete();
                        event.getJDA().getUserById(Long.valueOf(topicID)).openPrivateChannel().complete()
                                .sendFile(main.getLogDirectory().resolve(channel.getName()+ ".log").toFile())
                                .complete();
                        event.getJDA().getGuildById(main.getGuildId()).getTextChannelById(main.mgr.getConfig().getLogChannelID())
                                .sendFile(main.getLogDirectory().resolve(channel.getName()+ ".log").toFile(), new MessageBuilder().append("`").append(channel.getName()).append("` has been closed! Here's a log to reference")
                                        .build())
                                .complete();
                        channel.sendMessage("Deleting channel and sending logs in 30 seonds...");
                        ((TextChannel) event.getChannel()).delete().completeAfter(30, TimeUnit.SECONDS);
                    }
                    }
                }
            }
        }
    }
}