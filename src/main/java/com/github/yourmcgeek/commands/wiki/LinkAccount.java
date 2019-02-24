package com.github.yourmcgeek.commands.wiki;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

public class LinkAccount extends Command {

    private ShadowRewrite main;

    public LinkAccount(ShadowRewrite main) {
        this.main = main;
        this.name = "linkaccount";
        this.aliases = new String[]{"link", "accounts"};
        this.help = "Displays how to link your account with the forums!";
    }

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Linking Accounts")
                .setDescription("Use this link to see how to link your accounts with the forums, access <#260579843710779392> and the voice channels\n" + main.mgr.getConfig().getLinkingURL())
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));

        main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embed.build(), 10);
        event.getMessage().delete().complete();
    }
}