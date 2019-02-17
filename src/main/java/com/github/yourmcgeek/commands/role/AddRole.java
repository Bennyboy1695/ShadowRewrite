package com.github.yourmcgeek.commands.role;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.ArrayList;

public class AddRole extends Command {

    ShadowRewrite main;
    public AddRole(ShadowRewrite main) {
        this.main = main;
        this.name = "add";
        this.help = "Adds server role to user!";
    }

    @Override
    protected void execute(CommandEvent event) {
        ArrayList<String> serverRoles = main.mgr.getConfig().getServerRoles();
        String args = event.getArgs();
        EmbedBuilder embedSuc = new EmbedBuilder()
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                .setTitle("Success!")
                .setDescription("Successfully added role " + args);
        EmbedBuilder embedFail = new EmbedBuilder()
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                .setTitle("Failed!")
                .setDescription("Error adding role " + args + ". Make you spelled it correctly and it exists!");
        int y = 0;
        for (int x = 0; x != serverRoles.size(); x++) {
            if (serverRoles.get(x).equalsIgnoreCase(args)) {
                if (event.getGuild().getMember(event.getMember().getUser()).getRoles().stream().map(Role::getName).anyMatch(args::equalsIgnoreCase)) {
                    y = 2;
                }
                else {
                    event.getGuild().getController().addRolesToMember(event.getMember(), event.getJDA().getRolesByName(args, true)).complete();
                y = 1;
                }
            }
        }
        EmbedBuilder embed = new EmbedBuilder()
            .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
            .setTitle("Error!")
            .setDescription("You already have role " + args + ". If you are trying to remove it, run: /remove " + args);

        switch (y) {
            case 0:
                main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embedFail.build(), 10);
                break;
            case 1:
                main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embedSuc.build(), 10);
                break;
            case 2:
                main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embed.build(), 10);
                break;
            default:
                main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embedFail.build(), 10);
                break;
        }

        event.getMessage().delete().complete();
    }
}