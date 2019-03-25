package com.github.yourmcgeek.commands.wiki;

import com.github.yourmcgeek.ShadowRewrite;
import me.bhop.bjdautilities.command.result.CommandResult;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.List;

@Command(label = {"wiki"}, usage = "wiki", description = "Displays wiki hub link")

public class Wiki {

    private ShadowRewrite main;

    public Wiki(ShadowRewrite main) {
        this.main = main;
    }

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Wiki")
                .setDescription("Use this link to see access the wiki hub\n" + main.mgr.getConfig().getWikiURL())
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));

        main.getMessenger().sendEmbed(channel, embed.build(), 10);
        return CommandResult.success();
    }
}
