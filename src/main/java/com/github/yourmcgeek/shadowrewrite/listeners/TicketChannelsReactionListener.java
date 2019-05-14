package com.github.yourmcgeek.shadowrewrite.listeners;

import com.github.yourmcgeek.shadowrewrite.EmbedTemplates;
import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import me.bhop.bjdautilities.ReactionMenu;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.RestAction;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class TicketChannelsReactionListener extends ListenerAdapter {

    private ShadowRewrite main;

    public TicketChannelsReactionListener(ShadowRewrite main) {
        this.main = main;
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            TextChannel channel = (TextChannel) event.getChannel();
            if (channel.getParent().getIdLong() == main.getConfig().getConfigValue("supportCategoryId").getAsLong()) {
                if (channel.getTopic() != null) {
                    String cTopicFull = channel.getTopic();
                    String[] cTopicSplit = cTopicFull.split(" "); // https://regex101.com/r/r1zvJ6/1
                    String userId = cTopicSplit[5];
                    String supportMsgId = cTopicSplit[8];
                    String channelId = cTopicSplit[11];
                    Message message = event.getChannel().getMessageById(supportMsgId).complete();
                    if (message.getAuthor().isBot() && event.getReactionEmote().getName().equals("\u2705")) {
                        if (event.getReaction().isSelf())
                            return;
                        if (event.getMember().getUser().getIdLong() == Long.valueOf(userId) || event.getMember().getRoles().stream().map(Role::getName).anyMatch("Staff"::equalsIgnoreCase) ||
                                event.getMember().getRoles().stream().map(Role::getName).anyMatch("Developer"::equalsIgnoreCase)) {

                            RestAction<Message> message1 = event.getJDA().getGuildById(main.getGuildID()).getTextChannelById(channelId).getMessageById(supportMsgId);
                            Consumer<Message> callback = (msg) -> {
                                Message m = msg;
                                String ticket = m.getEmbeds().get(0).getFields().get(1).getValue();

                                try {
                                    event.getJDA().getUserById(Long.valueOf(userId)).openPrivateChannel().queue((privateChannel) -> privateChannel.sendMessage(new EmbedBuilder()
                                            .setTitle("Issue Completed")
                                            .setDescription("Because of this we have sent you a log file containing the history, so that you may look at it in case you encounter the issue again!")
                                            .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()))
                                            .addField("Next Step: ", "If the issue still persists, please create a new ticket!", false)
                                            .addField("Original Issue: ", ticket, false)
                                            .build()).queue());
                                    event.getJDA().getUserById(Long.valueOf(userId)).openPrivateChannel().queue((privateChannel ->
                                            privateChannel.sendFile(main.getLogDirectory().resolve(channel.getName() + ".log").toFile()).queue()));
                                } catch (Exception e) {
                                    main.getLogger().info("Support Log cannot be sent from ticket " + channel.getName() + " because the user has left the server. Sending only to supportlog channel");
                                }
                                event.getJDA().getGuildById(main.getGuildID()).getTextChannelById(main.getConfig().getConfigValue("logChannelId").getAsLong())
                                        .sendFile(main.getLogDirectory().resolve(channel.getName() + ".log").toFile(), new MessageBuilder().append("`").append(channel.getName()).append("` has been closed! The ticket was ```" + ticket + "``` Here's a log to reference")
                                                .build())
                                        .queue();
                                event.getJDA().getGuildById(main.getGuildID()).getTextChannelById(channelId).delete().queue();
                            };

                            ScheduledExecutorService scheduledTask = Executors.newScheduledThreadPool(2);

                            AtomicBoolean deleteChannel = new AtomicBoolean(true);
                            ReactionMenu reactionMenu = new ReactionMenu.Builder(event.getJDA())
                                    .setEmbed(new EmbedBuilder().setColor(Color.GREEN).setTitle("Deleting Channel").setDescription("Channel deletion started by " + event.getMember().getAsMention() + ", 60 seconds to abort!").setFooter("React with \uD83D\uDED1 to abort", null).build())
                                    .onClick("\uD83D\uDED1", (x, user) -> {
                                        if (user.getUser().getIdLong() == Long.valueOf(userId) || user.getRoles().stream().map(Role::getName).anyMatch("Staff"::equalsIgnoreCase) ||
                                                user.getRoles().stream().map(Role::getName).anyMatch("Developer"::equalsIgnoreCase)) {
                                            deleteChannel.set(false);
                                            x.removeReaction("\uD83D\uDED1");
                                            x.getMessage().setContent(new EmbedBuilder().setColor(Color.RED).setTitle("Cancelled!").setDescription("Ticket Deletion cancelled by " + user.getAsMention()).build());
                                            x.destroyIn(10);
                                        }
                                    })
                                    .onDisplay(display -> {
                                        scheduledTask.schedule(() -> {
                                            if (deleteChannel.get()) {
                                                message1.queue(callback);
                                            }
                                        }, 60, TimeUnit.SECONDS);
                                    }).buildAndDisplay(channel);
                            reactionMenu.destroyIn(60);

                        }
                    } else if (message.getAuthor().isBot() && event.getReactionEmote().getName().equals("\uD83D\uDD12")) {
                        if (event.getUser().isBot())
                            return;
                        channel.getManager().putPermissionOverride(event.getJDA().getGuildById(main.getGuildID()).getMemberById(Long.valueOf(userId)), 383040, 0L).complete();
                        Role publicRole = channel.getGuild().getPublicRole();
                        channel.putPermissionOverride(publicRole).setDeny(Permission.VIEW_CHANNEL).queue();
                        channel.getGuild().getRoles().forEach(role -> {
                            if (role.getName().equalsIgnoreCase("Staff") || role.getName().equalsIgnoreCase("Developer")) {
                                if (role.isPublicRole())
                                    return;
                                channel.putPermissionOverride(role).setAllow(Permission.VIEW_CHANNEL, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY).queue();
                            }
                        });
                        main.getMessenger().sendEmbed(channel, EmbedTemplates.CHANNEL_LOCKED.getBuilt());
                        main.getLogger().info("Locked channel: " + channel.getName());
                    }
                }
            }
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        if (event.isFromType(ChannelType.TEXT)) {
            TextChannel channel = (TextChannel) event.getChannel();
            if (channel.getParent().getIdLong() == main.getConfig().getConfigValue("supportCategoryId").getAsLong()) {
                if (event.getReaction().isSelf())
                    return;
                String cTopicFull = channel.getTopic();
                String[] cTopicSplit = cTopicFull.split(" "); // https://regex101.com/r/r1zvJ6/1
                String supportMsgId = cTopicSplit[8];
                Message message = event.getChannel().getMessageById(supportMsgId).complete();
                if (message.getAuthor().isBot() && event.getReactionEmote().getName().equals("\uD83D\uDD12")) {
                    if (event.getUser().isBot())
                        return;
                    channel.getManager().sync().complete();
                    main.getMessenger().sendEmbed(channel, EmbedTemplates.CHANNEL_UNLOCKED.getBuilt(), 10);
                    main.getLogger().info("Unlocked channel: " + channel.getName());
                }
            }
        }
    }
}