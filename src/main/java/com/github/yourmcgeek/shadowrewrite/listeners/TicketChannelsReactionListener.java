package com.github.yourmcgeek.shadowrewrite.listeners;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.RestAction;

import java.awt.*;
import java.util.function.Consumer;

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
                        String cTopicFull = channel.getTopic();
                        String[] cTopicSplit = cTopicFull.split(" ");
                        String userId = cTopicSplit[9];
                        String supportMsgId = cTopicSplit[12];
                        String channelId = cTopicSplit[15];

                        if (event.getMember().getUser().getIdLong() == Long.valueOf(userId) || event.getMember().getRoles().stream().map(Role::getName).anyMatch("Staff"::equalsIgnoreCase) ||
                                event.getMember().getRoles().stream().map(Role::getName).anyMatch("Developer"::equalsIgnoreCase)) {

                            RestAction<Message> message1 = event.getJDA().getGuildById(main.getGuildId()).getTextChannelById(channelId).getMessageById(supportMsgId);
                            Consumer<Message> callback = (msg) -> {
                                Message m = msg;
                                String ticket = m.getEmbeds().get(0).getFields().get(1).getValue();

                                event.getJDA().getUserById(Long.valueOf(userId)).openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                                        .setTitle("Issue Completed")
                                        .setDescription("Because of this we have sent you a log file containing the history, so that you may look at it in case you encounter the issue again!")
                                        .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                                        .addField("Next Step: ", "If the issue still persists, please create a new ticket!", false)
                                        .addField("Original Issue: ", ticket, false)
                                        .build()).queue();
                                event.getJDA().getUserById(Long.valueOf(userId)).openPrivateChannel().complete()
                                        .sendFile(main.getLogDirectory().resolve(channel.getName() + ".log").toFile())
                                        .queue();
                                event.getJDA().getGuildById(main.getGuildId()).getTextChannelById(main.mgr.getConfig().getLogChannelID())
                                        .sendFile(main.getLogDirectory().resolve(channel.getName() + ".log").toFile(), new MessageBuilder().append("`").append(channel.getName()).append("` has been closed! The ticket was ```" + ticket + "``` Here's a log to reference")
                                                .build())
                                        .queue();
                                event.getJDA().getGuildById(main.getGuildId()).getTextChannelById(channelId).delete().queue();
                            };
                            message1.queue(callback);
                        }
                    }
                }
            }
        }
    }
}