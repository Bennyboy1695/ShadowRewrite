package com.github.yourmcgeek.commands.support;

import com.github.yourmcgeek.ShadowRewrite;
import me.bhop.bjdautilities.command.CommandResult;
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

    private ShadowRewrite main;

    public SupportCommand(ShadowRewrite main) {
        this.main = main;
    }

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args) {
        RestAction<PrivateChannel> pmChannel = member.getUser().openPrivateChannel();
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                .setTitle("Support Ticket")
                .setDescription("Click the link to open your private message in order to create the ticket!\n" +
                        "Make sure you have private messages turned on for this server to receive a message from the bot!")
                .addField("Private Message", "https://discordapp.com/channels/@me/" + pmChannel.complete()
                .getIdLong(), false);

        main.getMessenger().sendEmbed(channel, embedBuilder.build(), 30);

        message.getAuthor().openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                .setTitle("Support Ticket Creation")
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                .setDescription("To create a ticket, please respond here and a channel will be created." +
                        "\nNote: Multiple messages will not be combined, so please type only one message.\n" +
                        "Also, if you upload a file, the file will be taken and sent in the support channel also!").build()).complete();
        message.delete().queue();
        return CommandResult.SUCCESS;
    }
}