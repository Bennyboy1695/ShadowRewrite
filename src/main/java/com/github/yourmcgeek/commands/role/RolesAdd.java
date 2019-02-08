package com.github.yourmcgeek.commands.role;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.Collections;

public class RolesAdd extends Command {

    private ShadowRewrite main;

    public RolesAdd(ShadowRewrite main) {
        this.main = main;
        this.name = "rolesadd";
        this.aliases = new String[]{"add"};
        this.help = "Add role to mentioned user";
        this.userPermissions = new Permission[]{Permission.MANAGE_ROLES};
    }

    @Override
    protected void execute(CommandEvent event) {
        event.getMessage().delete().complete();
        String args = event.getArgs();
        EmbedBuilder embed = new EmbedBuilder()
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));
        try {
            event.getMessage().getMentionedMembers().get(0).getRoles().add(Collections.singleton(event.getGuild().getRoles().stream(Role::getName).anyMatch(args::equalsIgnoreCase)));
            event.getMessage().getMentionedMembers().get(0).getRoles().add(event.getGuild().getRoles().stream().map(Role::getName).anyMatch(args::equalsIgnoreCase));
            embed.setTitle("Added role for " + event.getMessage().getMentionedMembers(), "");
            embed.addField("Role Added: ", args, true);
            main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embed.build(), 10);
        } catch (NullPointerException e) {
            embed.setTitle("Error adding role for " + event.getMessage().getMentionedMembers(), "");
            embed.addField("Role: ", args, true);
            embed.addField("Error", "Could not find Role by that name, check your list and try again!", false);
            main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embed.build(), 10);
        }
    }
}
