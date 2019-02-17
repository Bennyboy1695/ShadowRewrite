package com.github.yourmcgeek.commands.role.admin;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

public class AdminRemoveRole extends Command {

    private ShadowRewrite main;

    public AdminRemoveRole(ShadowRewrite main) {
        this.main = main;
        this.name = "adminremove";
        this.userPermissions = new Permission[]{Permission.MANAGE_ROLES};
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getMessage().getContentDisplay().split(" ");;
        EmbedBuilder embedSuc = new EmbedBuilder()
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                .setTitle("Success!")
                .setDescription("Successfully removed role " + args[2]);
        EmbedBuilder embedFail = new EmbedBuilder()
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                .setTitle("Failed!")
                .setDescription("Error removing role " + args[2] + ". Make you spelled it correctly and it exists!");
        EmbedBuilder embedError = new EmbedBuilder()
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                .setTitle("Error!")
                .setDescription("Error removing role " + args[2] + ". User does not have that role!");

        int y = 0;
        if (event.getGuild().getRoles().stream().map(Role::getName).anyMatch(args[2]::equalsIgnoreCase)) {
            event.getGuild().getController().removeRolesFromMember(event.getMessage().getMentionedMembers().get(0),
                    event.getGuild().getRolesByName(args[2], true)).complete();
            y = 1;
        }
        if (event.getMessage().getMentionedMembers().get(0).getRoles().stream().map(Role::getName).noneMatch(args[2]::equalsIgnoreCase)) { y = 2; }

        switch (y) {
            case 1:
                main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embedSuc.build(), 10);
                break;
            case 2:
                main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embedError.build(), 10);
                break;
            default:
                main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embedFail.build(), 10);
                break;
        }


        event.getMessage().delete().complete();
    }
}