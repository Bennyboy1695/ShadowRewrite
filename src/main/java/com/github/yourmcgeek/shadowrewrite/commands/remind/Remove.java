package com.github.yourmcgeek.shadowrewrite.commands.remind;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

@Command(value = "remove",  hideInHelp = true)
public class Remove {

    @Execute
    public CommandResult onRemove(Member member, TextChannel channel, Message message, String label, List<String> args, ShadowRewrite main) {

        return CommandResult.success();
    }
}