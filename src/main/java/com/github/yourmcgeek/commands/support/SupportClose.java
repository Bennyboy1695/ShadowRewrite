package com.github.yourmcgeek.commands.support;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;

import java.util.List;

public class SupportClose extends Command {

    private ShadowRewrite main;

    public SupportClose(ShadowRewrite main) {
        this.name = "Close";
        this.hidden = true;
        this.main = main;
    }

    @Override
    protected void execute(CommandEvent event) {
        List<Message> messageHistory = event.getChannel().getHistory().retrievePast(event.getChannel().getHistory().size()).complete();
        String[] channelName = new String{event.getChannel().getName().split("-");}



    }
}
