package com.github.yourmcgeek.commands.support;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

import java.awt.*;
import java.io.File;
import java.util.List;

public class SupportClose extends Command {

    private ShadowRewrite main;

    public SupportClose(ShadowRewrite main) {
        this.name = "Close";
        this.hidden = true;
        this.main = main;
    }

    @Override
    protected void execute(CommandEvent event) {
        List<Message> messageHistory = event.getChannel().getHistory().retrievePast(1).complete();
        String[] chanName = event.getChannel().getName().split("-");
        String nameId = chanName[1];
        event.getJDA().getUserById(nameId).openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                .setTitle("Issue Completed")
                .setDescription("Due to the ticket being closed, here is a log of conversation.")
                .addField("Next Step: ", "If your issue still continues, create a new ticket", false)
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                .build()).complete();
        Message message = new MessageBuilder().append(messageHistory).build();

        TextChannel channel = (TextChannel) event.getChannel();
        MessageAction messageAction = channel.sendFile(new File("%s.log"), event.getChannel().getName(), message);
        messageAction.complete();
        event.getJDA().getTextChannelById(event.getChannel().getId()).delete().complete();
    }
}