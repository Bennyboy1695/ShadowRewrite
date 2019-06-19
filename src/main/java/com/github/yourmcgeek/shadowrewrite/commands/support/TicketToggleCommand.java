package com.github.yourmcgeek.shadowrewrite.commands.support;

import com.github.yourmcgeek.shadowrewrite.EmbedTemplates;
import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

@Command(label = "toggleticket", usage = "toggleticket", description = "Disables or enables the ticket system!", permission = Permission.ADMINISTRATOR)
public class TicketToggleCommand {

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args, ShadowRewrite main) {
        if (main.isTicketsEnabled()) {
            main.setTicketsEnabled(false);
            main.getMessenger().sendEmbed(channel, EmbedTemplates.PRETTY_SUCCESSFULL.getEmbed().setDescription("Enabled tickets!").build(), 10);
        } else {
            main.setTicketsEnabled(true);
            main.getMessenger().sendEmbed(channel, EmbedTemplates.PRETTY_SUCCESSFULL.getEmbed().setDescription("Disabled tickets!").build(), 10);
        }
        return CommandResult.success();
    }
}
