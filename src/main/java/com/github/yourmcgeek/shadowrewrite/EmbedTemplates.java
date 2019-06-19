package com.github.yourmcgeek.shadowrewrite;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.time.Instant;

public enum EmbedTemplates {

    BASE(new EmbedBuilder().setTimestamp(Instant.now())),
    ERROR(BASE.getEmbed().setColor(Color.RED)),
    SUCCESS(BASE.getEmbed().setColor(Color.GREEN)),
    PRETTY_SUCCESSFULL(BASE.getEmbed().setColor(Color.CYAN)),

    CHANNEL_LOCKED(SUCCESS.getEmbed().setDescription("\uD83D\uDD12 Channel has been locked to just Staff and the Ticket Creator!")),
    CHANNEL_UNLOCKED(SUCCESS.getEmbed().setDescription("\uD83D\uDD13 Channel has been unlocked, it is now back to default ticket perms!")),
    NO_REMINDS(SUCCESS.getEmbed().setDescription("You do not have any reminders active, add one before you attempt to remove one!"));


    private final EmbedBuilder embed;
    EmbedTemplates(EmbedBuilder embed) {
        this.embed = embed;
    }

    public EmbedBuilder getEmbed() {
        return new EmbedBuilder(embed);
    }

    public EmbedBuilder getEmbed(Object... replacements) {
        EmbedBuilder embed = getEmbed();
        String description = embed.getDescriptionBuilder().toString();
        for (int i = 0; i < replacements.length; i++)
            description = description.replace("{" + i + "}", replacements[i].toString());
        return embed.setDescription(description);
    }
    public MessageEmbed getBuilt() {
        return getEmbed().build();
    }
}