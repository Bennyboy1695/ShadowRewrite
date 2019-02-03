package com.github.yourmcgeek.commands.support;

import com.github.yourmcgeek.ShadowRewrite;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.restaction.ChannelAction;

import java.time.LocalDateTime;

public class SupportCommand extends ListenerAdapter {

    private ShadowRewrite main;

    @Override
    public void onMessageRecievedEvent(MessageReceivedEvent event, ShadowRewrite main) {
        this.main = main;
        User author = event.getAuthor();
        String message = event.getMessage().getContentDisplay();

        if (event.isFromType(ChannelType.TEXT)) {
             if (event.getChannel().equals(main.mgr.getConfig().getSupportId())) {
                 EmbedBuilder builder = new EmbedBuilder()
                         .setColor(Integer.parseInt(main.mgr.getConfig().getColor()))
                         .setAuthor(author.getName(), "<%s>", String.valueOf(author.getIdLong()))
                         .setDescription(message + "\n\nWhen you are ready to close this message, please do /close." +
                                 "People with the following roles can close the ticket as well if necessary: Owner, " +
                                 "Senior-Advisor, Senior-Admin, Admin, Senior-Moderator, Moderator, Developer, Helper")
                         .setTimestamp(LocalDateTime.now());
                 event.getMessage().delete().queue();
                 String supportCategoryId = main.mgr.getConfig().getSupportCategoryId();
                 ChannelAction textChannel = main.getApi().getCategoryById(Integer.parseInt(supportCategoryId)).createTextChannel("ticket-" +
                         author.getId());
                 textChannel.complete();
                 MessageChannel channel = (MessageChannel) textChannel;
                 channel.sendMessage(builder.build()).complete();
             }
             else { return; }
        }

    }

}
