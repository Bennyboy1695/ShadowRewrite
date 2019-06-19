package com.github.yourmcgeek.shadowrewrite.commands.support;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import me.bhop.bjdautilities.EditableMessage;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.List;

@Command(label = {"supportsetup", "setupsupport"}, usage = "setupsupport", description = "Provides information that needs added to the configuration file for support to work properly", permission = Permission.ADMINISTRATOR, hideInHelp = true)
public class SupportSetup {

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args, ShadowRewrite main) {
        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            return CommandResult.noPermission();
        } else {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()));
            builder.setDescription("To make a support ticket please type /support. Please remember to always include the name of the server you are needing support on. When you do this a dedicated channel will be created for your issue. Once this is done Staff will contact you to try and resolve your issue.\n" +
                    "\n" +
                    "Tickets are not to be used for small questions that can be easily answered for these please use the this channel. Anything that requires in-depth staff interaction warrants creating a ticket. DO NOT abuse our ticket system doing so will result in a mute, kick or ban from our Discord.");
            EditableMessage editableMessage = main.getMessenger().sendEmbed(channel, builder.build(), 0);
            Message message2 = editableMessage;
            message2.pin().queue();
            channel.getManager().setTopic("Read the pinned message to learn how to get support!").queue();
            EmbedBuilder pmBuilder = new EmbedBuilder()
                    .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()))
                    .addField("SupportId", channel.getId(), true)
                    .addField("SupportCategoryId", message.getCategory().getId(), true)
                    .addField("GuildID", String.valueOf(channel.getGuild().getIdLong()), true)
                    .setDescription("Add these values in their corresponding places in your config file. Save the config," +
                            " then restart the bot!");
            try {
                message.getAuthor().openPrivateChannel().complete().sendMessage(pmBuilder.build()).queue();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return CommandResult.success();
        }
    }
}