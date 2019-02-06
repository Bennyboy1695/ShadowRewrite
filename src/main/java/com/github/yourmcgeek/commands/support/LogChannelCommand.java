package com.github.yourmcgeek.commands.support;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

public class LogChannelCommand extends Command {

    private ShadowRewrite main;
    public LogChannelCommand(ShadowRewrite main) {
        this.main = main;
        this.name = "logchannel";
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        main.getMessenger().sendEmbed((TextChannel) event.getChannel(), new EmbedBuilder()
            .setTitle("Log Channel ID")
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
            .addField("ID", String.valueOf(Long.valueOf(event.getChannel().getIdLong())), true)
            .setDescription("Add this in its corresponding place in your config file. Save the config," +
                    " then restart the bot!").build(), 30);
        event.getMessage().delete().complete();
    }
}