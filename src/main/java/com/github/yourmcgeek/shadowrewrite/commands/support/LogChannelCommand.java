package com.github.yourmcgeek.shadowrewrite.commands.support;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import me.bhop.bjdautilities.command.result.CommandResult;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.List;

@Command(label = {"log", "logchannel"}, usage = "log", description = "Prints out log channel id for configuration file", permission = Permission.ADMINISTRATOR, hideInHelp = true)
public class LogChannelCommand {

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args, ShadowRewrite main) {
        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            return CommandResult.noPermission();
        } else {
            main.getMessenger().sendEmbed(channel, new EmbedBuilder()
                    .setTitle("Log Channel ID")
                    .setColor(new Color(main.getMainConfig().getConfigValue("Red").getAsInt(), main.getMainConfig().getConfigValue("Blue").getAsInt(), main.getMainConfig().getConfigValue("Green").getAsInt()))
                    .addField("ID", String.valueOf(channel.getIdLong()), true)
                    .setDescription("Add this in its corresponding place in your config file. Save the config," +
                            " then restart the bot!").build(), 30);
            return CommandResult.success();
        }
    }
}