package com.github.yourmcgeek.commands.wiki;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

public class Wiki extends Command {

    private ShadowRewrite main;

    public Wiki(ShadowRewrite main) {
        this.main = main;
        this.name = "wiki";
        this.help = "Displays the direct link to the wiki";
    }


    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Wiki")
                .setDescription("Use this link to see access the wiki hub\n" + main.mgr.getConfig().getWikiURL())
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));

        main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embed.build(), 10);
        event.getMessage().delete().complete();
    }
}
