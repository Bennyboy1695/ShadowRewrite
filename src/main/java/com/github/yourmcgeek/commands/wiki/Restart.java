package com.github.yourmcgeek.commands.wiki;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

public class Restart extends Command {

    private ShadowRewrite main;

    public Restart(ShadowRewrite main) {
        this.main = main;
        this.name = "restart";
        this.aliases = new String[]{"wipes", "reset", "restarts", "resets"};
        this.help = "Displays information about wipes and restarts";
    }

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Wipes and Restarts")
                .setDescription("Use this link to see how often and when the servers restart/wipe\n" + main.mgr.getConfig().getRestartURL())
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));

        main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embed.build(), 10);
        event.getMessage().delete().queue();
    }
}