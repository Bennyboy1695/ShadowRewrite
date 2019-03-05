package com.github.yourmcgeek.listeners;

import com.github.yourmcgeek.ShadowRewrite;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.RestAction;

import java.awt.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RemindConfirmListener extends ListenerAdapter {

    private ShadowRewrite main;

    public RemindConfirmListener(ShadowRewrite main) {
        this.main = main;
    }
    private final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);

    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (!event.isFromType(ChannelType.TEXT)) {
            return;
        }
        else {
            for (int i = 0; i != main.getConfirmMessages().size(); i++) {
                JsonObject object = new Gson().fromJson(main.getConfirmMessages(), JsonObject.class);
                String msgId = object.get("messageID").getAsString();
                String userId = object.get("userID").getAsString();
                int timeAmt = object.get("time").getAsInt();
                String timeUnit = object.get("unit").getAsString();
                String content = object.get("content").getAsString();
                EmbedBuilder reminder = new EmbedBuilder()
                        .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                        .setTitle("Reminder")
                        .setDescription(content);
                EmbedBuilder confirmed = new EmbedBuilder()
                        .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                        .setTitle("Confirmed!")
                        .addField("Time", (timeAmt + timeUnit), true)
                        .setDescription(content);

                TimeUnit unit = null;

                switch (timeUnit) {
                    case "s":
                        unit = TimeUnit.SECONDS;
                        break;
                    case "m":
                        unit = TimeUnit.MINUTES;
                        break;
                    case "h":
                        unit = TimeUnit.HOURS;
                        break;
                    case "d":
                        unit = TimeUnit.DAYS;
                        break;
                    default:
                        unit = TimeUnit.MINUTES;
                        break;
                }

                MessageReaction.ReactionEmote reaction = event.getReaction().getReactionEmote();
                RestAction<Message> message = event.getChannel().getMessageById(msgId);
                if (message.complete().getAuthor().isBot() && reaction.getName().equals("\u2705")) {
                    if (message.complete().getEmbeds().contains(event.getJDA().getUserById(userId))) {
                        if (message.complete().getEmbeds().stream().map(MessageEmbed::getTitle).anyMatch(event.getJDA().getUserById(userId).getAsTag()::equalsIgnoreCase)) {
                            main.getMessenger().sendEmbed((TextChannel) event.getChannel(), confirmed.build(), 10);
                            ScheduledFuture<?> scheduledFuture = scheduler.schedule(new Runnable() {
                                @Override
                                public void run() {
                                    event.getUser().openPrivateChannel().complete().sendMessage(reminder.build()).queue();
                                }
                            }, timeAmt, unit);


                            try {
                                JsonObject object1 = new JsonObject();
                                object1.addProperty("userID", userId);
                                object1.addProperty("delay", scheduledFuture.getDelay(unit));
                                main.getRemindersJson().add(object1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

}
