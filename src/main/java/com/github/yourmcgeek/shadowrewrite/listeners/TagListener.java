package com.github.yourmcgeek.shadowrewrite.listeners;

import com.github.yourmcgeek.shadowrewrite.EmbedTemplates;
import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import me.bhop.bjdautilities.ReactionMenu;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class TagListener extends ListenerAdapter {

    private ShadowRewrite main;

    public TagListener(ShadowRewrite main) {
        this.main = main;
    }

    public void onGuildMessageRecieved(GuildMessageReceivedEvent event) {
        if (!event.getMessage().getMentionedRoles().isEmpty()) {
            for (Long channelID : main.mgr.getConfig().getChannelsToWatch()) {
                Channel channel = main.getJDA().getTextChannelById(channelID);
                if (event.getMessage().getChannel().getId().equals(channel.toString())) {
                    for (Long role : main.mgr.getConfig().getRolesToWatch()) {
                        Role role1 = main.getJDA().getRoleById(role);
                        if (event.getMessage().getMentionedRoles().contains(role1)) {
                            TextChannel textChannel = main.getJDA().getTextChannelById(main.mgr.getConfig().getPingChannel());
                            new ReactionMenu.Builder(main.getJDA())
                                    .onClick("\u247c", ReactionMenu::destroy)
                                    .setEmbed(EmbedTemplates.PRETTY_SUCCESSFULL.getEmbed().setTitle(role1.getName() + " was mentioned in " + event.getMessage().getChannel().getName())
                                            .setDescription(event.getMessage().getJumpUrl())
                                            .addField("Message: ", event.getMessage().getContentRaw(), true)
                                            .setFooter("Mentioned by: " + event.getMember().getEffectiveName(), event.getAuthor().getAvatarUrl()).build())
                                    .addStartingReaction("\u247c")
                                    .buildAndDisplay(textChannel);
                        }
                    }
                }
            }
        }

        if (!event.getMessage().getMentionedMembers().isEmpty()) {
            for (Long channelID : main.mgr.getConfig().getChannelsToWatch()) {
                Channel channel = main.getJDA().getTextChannelById(channelID);
                if (event.getMessage().getChannel().getId().equals(channel.toString())) {
                    for (Long user : main.mgr.getConfig().getUserMentionToWatch()) {
                        User user1 = main.getJDA().getUserById(user);
                        if (event.getMessage().getMentionedMembers().contains(user1)) {
                            TextChannel textChannel = main.getJDA().getTextChannelById(main.mgr.getConfig().getPingChannel());
                            new ReactionMenu.Builder(main.getJDA())
                                    .onClick("\u247c", ReactionMenu::destroy)
                                    .setEmbed(EmbedTemplates.PRETTY_SUCCESSFULL.getEmbed().setTitle(user1.getName() + " was mentioned in " + event.getMessage().getChannel().getName())
                                            .setDescription(event.getMessage().getJumpUrl())
                                            .addField("Message: ", event.getMessage().getContentRaw(), true)
                                            .setFooter("Mentioned by: " + event.getMember().getEffectiveName(), event.getAuthor().getAvatarUrl())
                                            .build())
                                    .addStartingReaction("\u247c")
                                    .buildAndDisplay(textChannel);
                        }
                    }
                }
            }
        }
    }
}