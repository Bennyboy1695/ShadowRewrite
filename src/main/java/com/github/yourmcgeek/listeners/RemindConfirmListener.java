package com.github.yourmcgeek.listeners;

import com.github.yourmcgeek.ShadowRewrite;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class RemindConfirmListener extends ListenerAdapter {

    private ShadowRewrite main;

    public RemindConfirmListener(ShadowRewrite main) {
        this.main = main;
    }

    public void onMessageReactionAddEvent(MessageReactionAddEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            TextChannel channel = (TextChannel) event.getChannel();
            Message message = null;
            message.getEmbeds().stream().map(MessageEmbed::getTitle).anyMatch("Please Confirm"::equalsIgnoreCase);
        }
    }

}
