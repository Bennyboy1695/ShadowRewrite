package com.github.yourmcgeek.commands.role;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.ArrayList;

public class RemoveRole extends Command {

    private ShadowRewrite main;

    public RemoveRole(ShadowRewrite main) {
        this.main = main;
        this.name = "remove";
        this.help = "Removes server roles from user!";
    }

    @Override
    protected void execute(CommandEvent event) {
        ArrayList<String> serverRoles = main.mgr.getConfig().getServerRoles();
        String args = event.getArgs();
        EmbedBuilder embedSuc = new EmbedBuilder()
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                .setTitle("Success!")
                .setDescription("Successfully removed role " + args);
        EmbedBuilder embedFail = new EmbedBuilder()
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                .setTitle("Failed!")
                .setDescription("Error removing role " + args + ". Make you spelled it correctly and it exists! If you need to see what roles you have run /roles");
        int y = 0;
        for (int x = 0; x != serverRoles.size(); x++) {
            if (serverRoles.get(x).equalsIgnoreCase(args)) {
                event.getGuild().getController().removeRolesFromMember(event.getMember(), event.getJDA().getRolesByName(args, true)).complete();
                y = 1;
            }
        }
        if (y == 1) {
            main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embedSuc.build(), 10);
        }
        else {
            main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embedFail.build(), 10);
        }
        event.getMessage().delete().complete();
    }
}
