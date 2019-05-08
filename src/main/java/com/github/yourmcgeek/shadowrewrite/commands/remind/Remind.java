package com.github.yourmcgeek.shadowrewrite.commands.remind;


import com.github.yourmcgeek.shadowrewrite.EmbedTemplates;
import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import me.bhop.bjdautilities.command.CommandHandler;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

@Command(label = {"remind", "reminder"}, children = {Add.class, List.class, Remove.class}, usage = "remind add|remove|list")
public class Remind {

    @Execute
    public CommandResult onRemind(Member member, TextChannel channel, Message message, String label, java.util.List<String> args, ShadowRewrite main, CommandHandler handler) {
        if (args.isEmpty())
            return CommandResult.invalidArguments();
        if (!args.get(0).toLowerCase().equals("list") || !args.get(0).toLowerCase().equals("add") || !args.get(0).toLowerCase().equals("remove")) {
            final CommandResult[] result = {CommandResult.success()};
            handler.getCommand(Add.class).ifPresent(cmd -> {
                result[0] = cmd.execute(member, channel, message, label, args);
            });
            if (result[0].equals(CommandResult.invalidArguments())) {
                EmbedTemplates.ERROR.getEmbed();
            }
            return result[0];
        }
        return CommandResult.success();
    }

}
