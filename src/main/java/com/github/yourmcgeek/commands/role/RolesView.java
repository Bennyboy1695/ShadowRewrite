package com.github.yourmcgeek.commands.role;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

public class RolesView extends Command {

    private ShadowRewrite main;
    public RolesView(ShadowRewrite main) {
        this.main = main;
        this.name = "roles";
        this.help = "View roles of yourself or mentioned player";
    }

    @Override
    protected void execute(CommandEvent event) {
        String args = event.getArgs();
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));

        try {
            embed.setTitle("Roles of " + event.getMessage().getMentionedMembers().get(0).getEffectiveName(), "");
            event.getMessage().getMentionedMembers().get(0).getRoles().forEach(l -> embed.appendDescription((l.getName() + "\n")));
            main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embed.build(), 15);
            event.getMessage().delete().complete();
        } catch (IndexOutOfBoundsException e) {
            embed.appendDescription(String.format("You need to mention a user in order to see their roles. Try again with the command %sroles @ShadowBot#7402", main.mgr.getConfig().getPrefix()));
            main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embed.build(), 10);
        }
        event.getMessage().delete().complete();
    }
}
