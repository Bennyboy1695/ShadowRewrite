package com.github.yourmcgeek.commands.support;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.RestAction;

import java.awt.*;

public class SupportCommand extends Command {

    private ShadowRewrite main;

    public SupportCommand(ShadowRewrite main) {
        this.main = main;
        this.name = "support";
        this.help = "Create a new support ticket";
        this.aliases = new String[]{"ticket"};
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        TextChannel channel = (TextChannel) event.getChannel();
        RestAction<PrivateChannel> pmChannel = event.getMember().getUser().openPrivateChannel();
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                .setTitle("Support Ticket")
                .setDescription("Click the link to open your private message in order to create the ticket!\n" +
                        "Make sure you have private messages turned on for this server to receive a message from the bot!")
                .addField("Private Message", "https://discordapp.com/channels/@me/" + pmChannel.complete()
                .getIdLong(), false);

        main.getMessenger().sendEmbed(channel, embedBuilder.build(), 30);

        main.getMessenger().sendPrivateMessage(event.getAuthor(), new EmbedBuilder()
        .setTitle("Support Ticket Creation")
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
        .setDescription("To create a ticket, please respond here and a channel will be created." +
                "\nNote: Multiple messages will not be combined, so please type only one message.\n" +
                "Also, if you upload a file, the file will be taken and sent in the support channel also!").build());
        event.getMessage().delete().complete();
    }
}