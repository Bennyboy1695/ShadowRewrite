package com.github.yourmcgeek.commands.support;

import com.github.yourmcgeek.ShadowRewrite;
import me.bhop.bjdautilities.command.CommandResult;
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

    private ShadowRewrite main;

    public LogChannelCommand(ShadowRewrite main) {
        this.main = main;
    }

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args) {
        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            return CommandResult.NO_PERMISSION;
        } else {
            main.getMessenger().sendEmbed(channel, new EmbedBuilder()
                    .setTitle("Log Channel ID")
                    .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                    .addField("ID", String.valueOf(Long.valueOf(channel.getIdLong())), true)
                    .setDescription("Add this in its corresponding place in your config file. Save the config," +
                            " then restart the bot!").build(), 30);
            return CommandResult.SUCCESS;
        }
    }
}