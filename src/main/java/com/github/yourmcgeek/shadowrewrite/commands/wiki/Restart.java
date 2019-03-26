package com.github.yourmcgeek.shadowrewrite.commands.wiki;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import me.bhop.bjdautilities.command.result.CommandResult;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.List;

@Command(label = {"resets", "restarts", "reset", "wipes", "restart"}, usage = "restart", description = "Displays information about wipes and restarts")

public class Restart {

    private ShadowRewrite main;
    public Restart(ShadowRewrite main) {
        this.main = main;
    }

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Wipes and Restarts")
                .setDescription("Use this link to see how often and when the servers restart/wipe\n" + main.mgr.getConfig().getRestartURL())
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));

        main.getMessenger().sendEmbed(channel, embed.build(), 10);
        return CommandResult.success();
    }
}