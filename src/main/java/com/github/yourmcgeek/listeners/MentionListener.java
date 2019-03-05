package com.github.yourmcgeek.listeners;

import com.github.yourmcgeek.ShadowRewrite;
import com.github.yourmcgeek.objects.util.FormatUtil;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MentionListener extends ListenerAdapter {

    private ShadowRewrite main;

    public MentionListener(ShadowRewrite main) {
        this.main = main;
    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!(event.getGuild().getMember(event.getMember().getUser()).getRoles().stream().map(Role::getName).anyMatch("Staff"::equalsIgnoreCase) ||
        event.getGuild().getMember(event.getMember().getUser()).getRoles().stream().map(Role::getName).anyMatch("Owner"::equalsIgnoreCase) ||
        event.getGuild().getMember(event.getMember().getUser()).getRoles().stream().map(Role::getName).anyMatch("Senior-Advisor"::equalsIgnoreCase))) {
            System.out.println(FormatUtil.filterEveryone(event.getMessage().getContentRaw()));

            FormatUtil.filterEveryone(event.getMessage().getContentRaw());
        }
    }

}