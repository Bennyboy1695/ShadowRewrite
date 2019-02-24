package com.github.yourmcgeek.commands.wiki;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

public class Tiquality extends Command {

    private ShadowRewrite main;
    public Tiquality(ShadowRewrite main) {
        this.main = main;
        this.name = "tiquality";
        this.aliases = new String[]{"tq", "claimtile", "cte", "tile"};
        this.help = "Displays wiki link to show how to claim Tile Entities with Tiquality";
    }

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Tiquality")
                .setDescription("Use this link to see how claim Tile Entities with Tiquality\n" + main.mgr.getConfig().getTQURL())
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));

        main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embed.build(), 10);
        event.getMessage().delete().complete();
    }
}