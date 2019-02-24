package com.github.yourmcgeek.commands.wiki;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

public class ChunkLoading extends Command {

    private ShadowRewrite main;

    public ChunkLoading(ShadowRewrite main) {
        this.main = main;
        this.name = "cl";
        this.aliases = new String[]{"chunks", "chunkload", "chunkloading"};
        this.help = "Displays the wiki link about chunkloading";
    }

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Chunk Loading")
                .setDescription("Use this link to see how to chunk load land\n" + main.mgr.getConfig().getCLURL())
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));

        main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embed.build(), 10);
        event.getMessage().delete().complete();
    }

}