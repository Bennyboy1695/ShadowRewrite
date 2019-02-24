package com.github.yourmcgeek.commands.support;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

public class SupportSetup extends Command {

    private ShadowRewrite main;

    public SupportSetup(ShadowRewrite main) {
        this.main = main;
        this.name = "SetupSupport";
        this.aliases = new String[]{};
        this.help = "Setup Support Main Channel";
        this.hidden = true;
        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getMessage().delete().complete();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));
        builder.setDescription("To make a support ticket please type /support. Please remember to always include the name of the server you are needing support on. When you do this a dedicated channel will be created for your issue. Once this is done Staff will contact you to try and resolve your issue.\n" +
                "\n" +
                "Tickets are not to be used for small questions that can be easily answered for these please use the this channel. Anything that requires in-depth staff interaction warrants creating a ticket. DO NOT abuse our ticket system doing so will result in a mute, kick or ban from our Discord.");
        Message message = main.getMessenger().sendEmbed((TextChannel) event.getChannel(), builder.build(), 0);
        message.pin().complete();
        ((TextChannel) event.getChannel()).getManager().setTopic("Read the pinned message to learn how to get support!").complete();
        EmbedBuilder pmBuilder = new EmbedBuilder()
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                .addField("SupportId", event.getChannel().getId(), true)
                .addField("SupportCategoryId", event.getMessage().getCategory().getId(), true)
                .addField("GuildID", String.valueOf(event.getGuild().getIdLong()), true)
                .setDescription("Add these values in their corresponding places in your config file. Save the config," +
                        " then restart the bot!");

        try {
            event.getAuthor().openPrivateChannel().complete().sendMessage(pmBuilder.build()).complete();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}