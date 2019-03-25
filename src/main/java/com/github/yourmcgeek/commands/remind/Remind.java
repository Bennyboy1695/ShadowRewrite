package com.github.yourmcgeek.commands.remind;

import com.github.yourmcgeek.ShadowRewrite;
import me.bhop.bjdautilities.command.CommandHandler;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

@Command(label = {"remind", "reminder"}, children = {Add.class, List.class, Remove.class})
public class Remind {

    private ShadowRewrite main;

    public Remind(ShadowRewrite main) {
        this.main = main;
    }

    @Execute
    public CommandResult onRemind(Member member, TextChannel channel, Message message, String label, java.util.List<String> args, CommandHandler handler) {
        if (!args.get(0).toLowerCase().equals("list") || !args.get(0).toLowerCase().equals("add") || !args.get(0).toLowerCase().equals("remove")) {
            handler.getCommand(Add.class).ifPresent(cmd -> cmd.execute(member, channel, message, label, args));
        }
        return CommandResult.success();
    }
}