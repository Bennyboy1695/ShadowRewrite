package com.github.yourmcgeek.commands;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;

public class LMGTFYCommand extends Command {

    private ShadowRewrite main;

    public LMGTFYCommand(ShadowRewrite main) {
        this.main = main;
        this.name = "lmgtfy";
        this.aliases = new String[]{"google"};
        this.requiredRole = "Staff";
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getMessage().delete().complete();
        String args = event.getArgs();

        String BASE_URL = "http://lmgtfy.com/?q=";
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Search Result")
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                .setDescription(BASE_URL + args.replaceAll(" ", "+"));
        main.getMessenger().sendEmbed(event.getTextChannel(), embed.build(), 10);
    }
}
