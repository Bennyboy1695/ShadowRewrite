package com.github.yourmcgeek.commands.wiki;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

public class Crate extends Command {

    private ShadowRewrite main;

    public Crate(ShadowRewrite main) {
        this.main = main;
        this.name = "crate";
        this.aliases = new String[]{"crates"};
        this.help = "Displays the direct link to the Crates wiki page!";
    }


    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Crates")
                .setDescription("Use this link to see access the Crates wiki page\n" + main.mgr.getConfig().getCrateURL())
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));

        main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embed.build(), 10);
        event.getMessage().delete().queue();


    }
}