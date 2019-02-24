package com.github.yourmcgeek.commands.wiki;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

public class Relocate extends Command {

    private ShadowRewrite main;

    public Relocate(ShadowRewrite main) {
        this.main = main;
        this.name = "relocate";
        this.aliases = new String[]{"relocation"};
        this.help = "Displays the direct link to the Relocation page";
    }


    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Relocation")
                .setDescription("Use this link to see access the relocation page hub\n" + main.mgr.getConfig().getRelocateURL())
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));

        main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embed.build(), 10);
        event.getMessage().delete().complete();


    }
}