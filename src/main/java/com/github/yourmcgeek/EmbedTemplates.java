package com.github.yourmcgeek;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.time.Instant;

public enum EmbedTemplates {

    BASE(new EmbedBuilder().setTimestamp(Instant.now())),
    ERROR(BASE.getEmbed().setColor(Color.RED)),
    SUCCESS(BASE.getEmbed().setColor(Color.GREEN)),
    PRETTY_SUCCESSFULL(BASE.getEmbed().setColor(Color.CYAN));

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