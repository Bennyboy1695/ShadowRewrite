package com.github.yourmcgeek.commands.wiki;

import com.github.yourmcgeek.ShadowRewrite;
import me.bhop.bjdautilities.command.CommandResult;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.List;

@Command(label = {"tile", "cte", "claimtile", "tq"}, usage = "tiquality", description = "Displays wiki link to show how to claim Tile Entities with Tiquality")

public class Tiquality {

    private ShadowRewrite main;
    public Tiquality(ShadowRewrite main) {
        this.main = main;
    }

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Tiquality")
                .setDescription("Use this link to see how claim Tile Entities with Tiquality\n" + main.mgr.getConfig().getTQURL())
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));

        main.getMessenger().sendEmbed(channel, embed.build(), 10);
        message.delete().queue();
        return CommandResult.SUCCESS;
    }
}