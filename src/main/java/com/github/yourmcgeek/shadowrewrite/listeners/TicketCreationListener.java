package com.github.yourmcgeek.shadowrewrite.listeners;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import com.google.gson.JsonElement;
import me.bhop.bjdautilities.ReactionMenu;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class TicketCreationListener extends ListenerAdapter {

    private ShadowRewrite main;
    private int userCount;

    public TicketCreationListener(ShadowRewrite main) {
        this.main = main;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/YY");
        if (event.getAuthor().isBot() || event.getChannel().getIdLong() != main.getConfig().getConfigValue("TicketCreationChannelID").getAsLong() || event.getMessage().getContentRaw().contains("supportsetup") || event.getMessage().getContentRaw().contains("setupsupport"))
            return;
        Member member = event.getJDA().getGuildById(main.getGuildID()).getMember(event.getAuthor());

        for (Guild.Ban bans : event.getJDA().getGuildById(main.getGuildID()).getBanList().complete()) {
            if (bans.getUser().getIdLong() == member.getUser().getIdLong())
                return;
        }

        for (TextChannel channel : main.getJDA().getCategoryById(Long.valueOf(main.getConfig().getConfigValue("supportCategoryId").getAsString())).getTextChannels()) {
            if (channel.getName().contains(event.getAuthor().getName())) {
                userCount++;
                if (userCount >= 2) {
                    main.getMessenger().sendMessage(event.getChannel(), "No channel has been created because you already have a ticket open! Please respond within the ticket to resolve that issue first!", 10);
                    return;
                }
            }
        }

        String userMessage = event.getMessage().getContentRaw();

        TextChannel supportChannel = (TextChannel) event.getJDA().getCategoryById(main.getConfig().getConfigValue("supportCategoryId").getAsLong())
                .createTextChannel(member.getEffectiveName() + "-" + ThreadLocalRandom.current().nextInt(99999)).complete();

        EmbedBuilder message = new EmbedBuilder()
                .addField("Author: ", member.getAsMention(), true)
                .addField("Ticket: ", userMessage, true)
                .addField("Username: ", "Run `" + main.getPrefix() + "username <Username>` to set this field", true)
                .addField("UUID: ", "Run `" + main.getPrefix() + "username <Username>` to set this field", true)
                .addField("Server:", "Run `" + main.getPrefix() + "server <Server>` to set this field", true)
                .setFooter("If you are finished, please click \u2705. All staff and developers can close the ticket also.", event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()));

        ReactionMenu supportMessage = new ReactionMenu.Builder(event.getJDA()).setEmbed(message.build()).addStartingReaction("\u2705").addStartingReaction("\uD83D\uDD12").buildAndDisplay(supportChannel);
        supportChannel.getManager().setTopic("Creation date: " + supportChannel.getCreationTime().format(dateFormat) + " Authors ID: " + event.getAuthor().getIdLong() + " Message ID: " + supportMessage.getMessage().getIdLong() + " Channel ID: " + supportChannel.getIdLong()).queue();
        for (Message.Attachment attachment : event.getMessage().getAttachments()) {
            String[] fileName = attachment.getFileName().split("\\.");
            for (JsonElement blacklistArray : main.getConfig().getConfigValue("blacklistFiles").getAsJsonArray()) {

                if (blacklistArray.getAsString().equalsIgnoreCase(fileName[1])) {
                    try {
                        if (!new File(main.getLogDirectory().toFile(), "attachments").exists()) {
                            new File(main.getLogDirectory().toFile(), "attachments").mkdir();
                        }
                        attachment.download(new File(main.getLogDirectory().toFile() + "/attachments/", attachment.getFileName() + ".log"));
                        supportChannel.sendFile(new File(main.getLogDirectory().toFile() + "/attachments/", attachment.getFileName() + ".log")).complete();
                        main.getMessenger().sendMessage(event.getChannel(), event.getMessage().getAuthor() + " has sent a file called " + attachment.getFileName() + ".log");
                    } catch (Exception e) {
                        main.getLogger().error("Error with PrivateMessageListener ", e);
                    }
                } else {
                    if (Files.exists(main.getAttachmentDir().resolve(attachment.getFileName()))) {
                        main.getLogger().info("Renaming attachment as one already exists!");
                        String rename = fileName[0] + ThreadLocalRandom.current().nextInt(99999) + "." + fileName[1];

                        attachment.download(new File(String.valueOf(main.getAttachmentDir().resolve(rename))));
                        supportChannel.sendFile(new File(String.valueOf(main.getAttachmentDir().resolve(rename)))).complete();
                        main.getLogger().info(attachment.getFileName() + " was renamed to " + rename);
                    } else {
                        attachment.download(new File(main.getLogDirectory().toFile() + "/attachments/", attachment.getFileName()));
                        supportChannel.sendFile(new File(main.getLogDirectory().toFile() + "/attachments/", attachment.getFileName())).complete();
                    }
                }
            }
        }

        event.getMessage().delete().queue();

        supportMessage.getMessage().pin().complete();
        supportChannel.getHistory().retrievePast(1).queue(l -> l.forEach(m -> m.delete().queue()));
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Support Channel")
                .setDescription("If you would prefer this ticket to be private, or concerns a dupe, glitch, bug, or contribution payment please react in your ticket channel with \uD83D\uDD12 to only allow staff to view the ticket. ")
                .addField("Ticket", "https://discordapp.com/channels/" + main.getGuildID() + "/" + supportChannel.getIdLong(), false)
                .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()));
        main.getMessenger().sendEmbed(event.getChannel(), embedBuilder.build(), 10);
    }
}