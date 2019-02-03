package com.github.yourmcgeek.commands.support;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class SupportCommand extends Command {


    public SupportCommand() {
        this.name = "Support";
        this.aliases = new String[]{"Ticket"};
        this.help = "Open a new support channel to communicate with staff members";
    }

    @Override
    protected void execute(CommandEvent event) {

    }
}
