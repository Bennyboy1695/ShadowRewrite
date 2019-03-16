package com.github.yourmcgeek.commands.wiki;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

public class CrashReport extends Command {

    private ShadowRewrite main;
    public CrashReport(ShadowRewrite main) {
        this.main = main;
        this.name = "crashreport";
        this.aliases = new String[]{"crash", "crashreports"};
        this.help = "Displays wiki link to show how to properly find and share crash reports";
    }

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Crash Reports")
                .setDescription("Use this link to see how to find and share crash reports\n" + main.mgr.getConfig().getCrashURL())
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));

        main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embed.build(), 10);
        event.getMessage().delete().queue();
    }

}