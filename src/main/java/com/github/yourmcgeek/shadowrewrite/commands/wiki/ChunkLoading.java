package com.github.yourmcgeek.shadowrewrite.commands.wiki;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;

import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.List;

@Command(label = {"chunkloading", "cl"}, usage = "cl", description = "Displays wiki link for ChunkLoading")
public class ChunkLoading {


    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args, ShadowRewrite main) {
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Chunk Loading")
                .setDescription("Use this link to see how to chunk load land\n" + main.getConfig().getConfigValue("clURL").getAsString())
                .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()));

        main.getMessenger().sendEmbed(channel, embed.build(), 10);
        return CommandResult.success();
    }
}