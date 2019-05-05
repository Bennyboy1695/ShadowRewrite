package com.github.yourmcgeek.shadowrewrite.commands.support;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import me.bhop.bjdautilities.EditableMessage;
import me.bhop.bjdautilities.ReactionMenu;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.json.JSONObject;

import java.util.List;

@Command(label = "server", usage = "/server Events", description = "Sets the server that the issue occurs on", minArgs = 1, hideInHelp = true)
public class ServerCommand {

    private ShadowRewrite main;

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args, ShadowRewrite main) {

        String[] split = channel.getTopic().split(" ");
        long messageId = Long.valueOf(split[8]);
        long authorId = Long.valueOf(split[5]);
        channel.getMessageById(messageId).queue(message1 -> {
            ReactionMenu reaction = new ReactionMenu.Import(message1).build();
            EditableMessage originalMessage = reaction.getMessage();
            if (message.getAuthor().getIdLong() == authorId) {
                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setFooter(originalMessage.getEmbeds().get(0).getFooter().getText(), originalMessage.getEmbeds().get(0).getFooter().getProxyIconUrl())
                        .setColor(originalMessage.getEmbeds().get(0).getColorRaw())
                        .addField(originalMessage.getEmbeds().get(0).getFields().get(0).getName(), originalMessage.getEmbeds().get(0).getFields().get(0).getValue(), true)
                        .addField(originalMessage.getEmbeds().get(0).getFields().get(1).getName(), originalMessage.getEmbeds().get(0).getFields().get(1).getValue(), true)
                        .addField(originalMessage.getEmbeds().get(0).getFields().get(2).getName(), originalMessage.getEmbeds().get(0).getFields().get(2).getValue(), true)
                        .addField(originalMessage.getEmbeds().get(0).getFields().get(3).getName(), originalMessage.getEmbeds().get(0).getFields().get(3).getValue(), true)
                        .addField(originalMessage.getEmbeds().get(0).getFields().get(4).getName(), args.get(0), true);
                reaction.getMessage().setContent(embedBuilder.build());
            } else {
                message.delete().queue();
            }
        });
        return CommandResult.success();
    }
}