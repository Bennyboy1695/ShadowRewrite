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

@Command(label = {"crates", "crate"}, usage = "crate", description = "Displays the direct link to the Crates wiki page!")

public class Crate {

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args, ShadowRewrite main) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Crates")
                .setDescription("Use this link to see access the Crates wiki page\n" + main.getMainConfig().getConfigValue("crateURL").getAsString())
                .setColor(new Color(main.getMainConfig().getConfigValue("Red").getAsInt(), main.getMainConfig().getConfigValue("Blue").getAsInt(), main.getMainConfig().getConfigValue("Green").getAsInt()));
        main.getMessenger().sendEmbed(channel, embed.build(), 10);
        return CommandResult.success();
    }
}