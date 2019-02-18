/*
 *
 * Original file from NetBans Support Bot. Used with permission, original source here
 * https://github.com/NetBans/SupportBot/
 *
 */

package com.github.yourmcgeek.objects.message;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Messenger {

    private final ScheduledExecutorService executorService;

    public Messenger() {
        this.executorService = Executors.newScheduledThreadPool(2);
    }

    public Message sendMessage(TextChannel channel, String message) {
        return this.sendMessage(channel, new MessageBuilder().append(message).build(), 0);
    }

    public Message sendMessage(TextChannel channel, String message, int lifetime) {
        return this.sendMessage(channel, new MessageBuilder().append(message).build(), lifetime);
    }

    public Message sendEmbed(TextChannel channel, MessageEmbed embed) {
        return this.sendMessage(channel, new MessageBuilder().setEmbed(embed).build(), 0);
    }

    public Message sendEmbed(TextChannel channel, MessageEmbed embed, int lifetime) {
        return this.sendMessage(channel, new MessageBuilder().setEmbed(embed).build(), lifetime);
    }

    public Message sendMessage(TextChannel channel, Message message, int lifetime) {

        Message sentMessage = channel.sendMessage(message).complete();
        if (lifetime != 0)
            this.delMessage(channel, sentMessage.getIdLong(), lifetime);

        return sentMessage;
    }

    public void delMessage(TextChannel channel, long id, int lifetime) {
        AtomicLong messageID = new AtomicLong();
        messageID.set(id);
        this.executorService.schedule(() -> channel.getMessageById(messageID.get()).queue(m -> m.delete().queue()), lifetime, TimeUnit.SECONDS);
    }

    public Message sendPrivateMessage(User user, MessageEmbed embed) {
        PrivateChannel channel = user.openPrivateChannel().complete();
        return channel.sendMessage(new MessageBuilder().setEmbed(embed).build()).complete();
    }

    public Message sendPrivateMessage(User user, Message message) {
        PrivateChannel channel = user.openPrivateChannel().complete();
        return channel.sendMessage(message).complete();
    }

    public Message sendPrivateMessage(User user, String message) {
        PrivateChannel channel = user.openPrivateChannel().complete();
        return channel.sendMessage(new MessageBuilder().append(message).build()).complete();
    }

}
