package com.github.yourmcgeek.listeners;

import com.github.yourmcgeek.ShadowRewrite;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.RestAction;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class RemindConfirmListener extends ListenerAdapter {

    private ShadowRewrite main;

    public RemindConfirmListener(ShadowRewrite main) {
        this.main = main;
    }
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (!event.isFromType(ChannelType.TEXT)) {
            return;
        }
        else {
            for (int i = 0; i != main.getConfirmMessages().size(); i++) {
                JsonObject object = new Gson().fromJson(main.getConfirmMessages(), JsonObject.class);
                String msgId = object.get("messageID").getAsString();
                Object userId = object.get("userID");
                int timeAmt = object.get("timeAmt").getAsInt();
                String content = object.get("content").getAsString();
                EmbedBuilder embed = new EmbedBuilder()
                        .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                        .setTitle("Reminder")
                        .setDescription(content);
                MessageReaction.ReactionEmote emote = event.getReaction().getReactionEmote();
                RestAction<Message> message = event.getChannel().getMessageById(msgId);
                if (message.complete().getAuthor().isBot() && emote.getName().equals("\u2705")) {
                    // check if reaction came from user with name set in Author Field of Embed
                }
            }
        }
    }

}
