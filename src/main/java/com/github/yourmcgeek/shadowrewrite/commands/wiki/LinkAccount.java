package com.github.yourmcgeek.shadowrewrite.commands.wiki;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import me.bhop.bjdautilities.command.result.CommandResult;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.List;

@Command(label = {"link", "accounts", "linkaccount"}, usage = "link", description = "Displays how to link your account with the forums!")
public class LinkAccount {

    private ShadowRewrite main;

    public LinkAccount(ShadowRewrite main) {
        this.main = main;
    }

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Linking Accounts")
                .setDescription("Use this link to see how to link your accounts with the forums, access <#260579843710779392> and the voice channels\n" + main.mgr.getConfig().getLinkingURL())
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));

        main.getMessenger().sendEmbed(channel, embed.build(), 10);
        return CommandResult.success();
    }
}