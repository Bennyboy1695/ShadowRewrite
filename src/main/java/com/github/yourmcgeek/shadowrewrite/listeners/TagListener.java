package com.github.yourmcgeek.shadowrewrite.listeners;

import com.github.yourmcgeek.shadowrewrite.EmbedTemplates;
import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.bhop.bjdautilities.ReactionMenu;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TagListener extends ListenerAdapter {

    private ShadowRewrite main;

    public TagListener(ShadowRewrite main) {
        this.main = main;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!event.getMessage().getMentionedRoles().isEmpty()) {
            for (JsonElement channelId : main.getConfig().getConfigValue("channelsToWatch").getAsJsonArray()) {
                if (channelId.getAsLong() == event.getChannel().getIdLong()) {
                    for (Long role : getRoleMentions()) {
                        Role role1 = main.getJDA().getRoleById(role);
                        if (event.getMessage().getMentionedRoles().contains(role1)) {
                            TextChannel textChannel = main.getJDA().getTextChannelById(main.getConfig().getConfigValue("redirectChannelID").getAsLong());
                            new ReactionMenu.Builder(main.getJDA())
                                    .onClick("\u274C", ReactionMenu::destroy)
                                    .setEmbed(EmbedTemplates.PRETTY_SUCCESSFULL.getEmbed().setTitle(role1.getName() + " was mentioned in " + event.getMessage().getTextChannel().getName() + "!").setDescription(event.getMessage().getJumpUrl()).addField("Message: ", event.getMessage().getContentRaw(), true).setFooter("Mentioned by " + event.getMember().getEffectiveName(), event.getAuthor().getAvatarUrl()).build())
                                    .addStartingReaction("\u274C")
                                    .buildAndDisplay(textChannel);
                        }
                    }
                }
            }
        }
        if (!event.getMessage().getMentionedUsers().isEmpty()) {
            for (JsonElement channelId : main.getConfig().getConfigValue("channelsToWatch").getAsJsonArray()) {
                if (channelId.getAsLong() == event.getChannel().getIdLong()) {
                    for (Long user : getUserMentions()) {
                        User user1 = main.getJDA().getUserById(user);
                        if (event.getMessage().getMentionedUsers().contains(user1)) {
                            TextChannel textChannel = main.getJDA().getTextChannelById(main.getConfig().getConfigValue("redirectChannelID").getAsLong());
                            new ReactionMenu.Builder(main.getJDA())
                                    .onClick("\u274C", ReactionMenu::destroy)
                                    .setEmbed(EmbedTemplates.PRETTY_SUCCESSFULL.getEmbed().setTitle(user1.getName() + " was mentioned in " + event.getMessage().getTextChannel().getName() + "!").setDescription(event.getMessage().getJumpUrl()).addField("Message: ", event.getMessage().getContentRaw(), false).setFooter("Mentioned by " + event.getMember().getEffectiveName(), event.getAuthor().getAvatarUrl()).build())
                                    .addStartingReaction("\u274C")
                                    .buildAndDisplay(textChannel);
                        }
                    }
                }
            }
        }
    }

    private List<Long> getRoleMentions() {
        List<Long> longs = new ArrayList<>();
        JsonObject mentions = main.getConfig().getConfigValue("mentions");
        JsonArray roles = mentions.get("roles").getAsJsonArray();
        for (Object object : roles) {
            JsonElement entry = (JsonElement) object;
            longs.add(entry.getAsLong());
        }
        return longs;
    }

    private List<Long> getUserMentions() {
        List<Long> longs = new ArrayList<>();
        JsonObject mentions = main.getConfig().getConfigValue("mentions");
        JsonArray users = mentions.get("users").getAsJsonArray();
        for (Object object : users) {
            JsonElement entry = (JsonElement) object;
            longs.add(entry.getAsLong());
        }
        return longs;
    }
}