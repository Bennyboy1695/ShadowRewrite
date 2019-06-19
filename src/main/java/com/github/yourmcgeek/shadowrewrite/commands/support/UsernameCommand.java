package com.github.yourmcgeek.shadowrewrite.commands.support;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import com.google.gson.JsonObject;
import me.bhop.bjdautilities.EditableMessage;
import me.bhop.bjdautilities.ReactionMenu;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

import static com.github.yourmcgeek.shadowrewrite.utils.Util.isValidUsername;

@Command(label = "username", usage = "/username YourMCGeek", description = "Sets the username of a ticket", minArgs = 1, hideInHelp = true)
public class UsernameCommand {

    private ShadowRewrite main;
    private JsonObject data;

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args, ShadowRewrite main) {

        String[] split = channel.getTopic().split(" ");
        long messageId = Long.valueOf(split[8]);
        long authorId = Long.valueOf(split[5]);
        channel.getMessageById(messageId).queue(message1 -> {
            ReactionMenu reaction = new ReactionMenu.Import(message1).build();
            EditableMessage originalMessage = reaction.getMessage();
            if (message.getAuthor().getIdLong() == authorId) {
                if (isValidUsername(args.get(0)) != null) {
                    data = isValidUsername(args.get(0));
                    String regex = "(.{8})(.{4})(.{4})(.{4})(.{12})";
                    String uuid = data.get("id").getAsString();
                    String formattedUUID = uuid.replaceAll(regex, "$1-$2-$3-$4-$5");
                    EmbedBuilder embedBuilder = new EmbedBuilder()
                            .setFooter(originalMessage.getEmbeds().get(0).getFooter().getText(), originalMessage.getEmbeds().get(0).getFooter().getProxyIconUrl())
                            .setColor(originalMessage.getEmbeds().get(0).getColorRaw())
                            .addField(originalMessage.getEmbeds().get(0).getFields().get(0).getName(), originalMessage.getEmbeds().get(0).getFields().get(0).getValue(), true)
                            .addField(originalMessage.getEmbeds().get(0).getFields().get(1).getName(), originalMessage.getEmbeds().get(0).getFields().get(1).getValue(), true)
                            .addField(originalMessage.getEmbeds().get(0).getFields().get(2).getName(), data.get("name").getAsString(), true)
                            .addField(originalMessage.getEmbeds().get(0).getFields().get(3).getName(), ("[" + formattedUUID + "](https://mcuuid.net/?q=" + formattedUUID + ")"), true)
                            .addField(originalMessage.getEmbeds().get(0).getFields().get(4).getName(), originalMessage.getEmbeds().get(0).getFields().get(4).getValue(), true);

                    reaction.getMessage().setContent(embedBuilder.build());
                } else {
                    main.getMessenger().sendMessage(channel, "Not a valid username, try again", 10);
                }
            } else {
                message.delete().queue();
            }
        });
        return CommandResult.success();
    }
}