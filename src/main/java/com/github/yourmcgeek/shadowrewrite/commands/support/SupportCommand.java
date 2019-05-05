package com.github.yourmcgeek.shadowrewrite.commands.support;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import me.bhop.bjdautilities.command.result.CommandResult;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.RestAction;

import java.awt.*;
import java.util.List;

@Command(label = {"support", "ticket"}, usage = "support", description = "Opens a support ticket")
public class SupportCommand {

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args, ShadowRewrite main) {
        RestAction<PrivateChannel> pmChannel = member.getUser().openPrivateChannel();
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()))
                .setTitle("Support Ticket")
                .setDescription("Click the link to open your private message in order to create the ticket!\n" +
                        "Make sure you have private messages turned on for this server to receive a message from the bot!")
                .addField("Private Message", "https://discordapp.com/channels/@me/" + pmChannel.complete()
                .getIdLong(), false);

        main.getMessenger().sendEmbed(channel, embedBuilder.build(), 30);

        message.getAuthor().openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                .setTitle("Support Ticket Creation")
                .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()))
//                .setDescription("To create a ticket, please respond here and a channel will be created." +
//                        "\nNote: Multiple messages will not be combined, so please type only one message.\n" +
//                        "Also, if you upload a file, the file will be taken and sent in the support channel also!")
                .setDescription("Please enter your username")
                .build()).complete();
        return CommandResult.success();
    }
}