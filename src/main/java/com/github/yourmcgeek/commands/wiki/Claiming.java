package com.github.yourmcgeek.commands.wiki;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

public class Claiming extends Command {

    private ShadowRewrite main;
    public Claiming(ShadowRewrite main) {
        this.main = main;
        this.name = "claiming";
        this.aliases = new String[]{"claim"};
        this.help = "Displays wiki link to show how to claim land";
    }

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Claiming")
                .setDescription("Use this link to see how to claim land\n" + main.mgr.getConfig().getClaimURL())
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));

        main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embed.build(), 10);
        event.getMessage().delete().complete();
    }

}