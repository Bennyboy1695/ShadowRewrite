package com.github.yourmcgeek.listeners;

import com.github.yourmcgeek.ShadowRewrite;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

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
                        for (Member member : message.getMentionedMembers()) {
                            if (event.getMember() == member || event.getMember().getRoles().contains(event.getGuild().getRolesByName("Staff", true)) ||
                                    event.getMember().getRoles().contains(event.getGuild().getRolesByName("Developer", true))) {
                                Message historyMessage = channel.getHistory().retrievePast(1).complete().get(0);
                                String reason = "";
                                String memberMention = "";
                                if (member.getUser().getIdLong() == event.getMember().getUser().getIdLong()) {
                                    memberMention = "you";
                                } else {
                                    memberMention = event.getMember().getAsMention();
                                }
                                if (historyMessage.getContentRaw().startsWith("~")){
                                    reason = event.getChannel().getName() + " was deleted by " + memberMention + " for reason: " + historyMessage.getContentRaw().replace("~", "");
                                } else {
                                    reason = memberMention + " deleted `" + channel.getName() + "` because the issue was marked complete!";
                                }
                                member.getUser().openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                                        .setTitle("Issue Completed")
                                        .setDescription(reason.replaceAll("you", "You") + " \nBecause of this we have sent you a log file containing the history, so that you may look at it incase you encounter the issue again!")
                                        .addField("Next Step: ", "If the issue still persists, please create a github issue using the link provided below!\n" +
                                                "https://github.com/NetBans/NetBans/issues/new", false)
                                        .build()).complete();
                                member.getUser().openPrivateChannel().complete()
                                        .sendFile(main.getLogDirectory().resolve(channel.getName()+ ".log").toFile())
                                        .complete();
                                event.getJDA().getGuildById(main.getGuildId()).getTextChannelById(main.mgr.getConfig().getLogChannelID())
                                        .sendFile(main.getLogDirectory().resolve(channel.getName()+ ".log").toFile(), new MessageBuilder()
                                                .append(reason.replaceAll("you", event.getMember().getAsMention()))
                                                .build())
                                        .complete();
                                channel.delete().reason("Issue completed!").complete();
                            }
                        }
                    }
                }
            }
        }
    }
}