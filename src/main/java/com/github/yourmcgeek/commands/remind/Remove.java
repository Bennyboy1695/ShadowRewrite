package com.github.yourmcgeek.commands.remind;

import com.github.yourmcgeek.ShadowRewrite;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

@Command("remove")
public class Remove {

    private ShadowRewrite main;

    public Remove(ShadowRewrite main) {
        this.main = main;
    }

    @Execute
    public CommandResult onRemove(Member member, TextChannel channel, Message message, String label, List<String> args) {
        return CommandResult.success();
    }
}