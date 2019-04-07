package com.github.yourmcgeek.shadowrewrite.listeners;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class PrivateMessageListener extends ListenerAdapter {

    private ShadowRewrite main;
    private int userCount;


    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/YY");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        if (event.getAuthor().isBot()) return;

        String userMessage = event.getMessage().getContentRaw();

        Member member = event.getJDA().getGuildById(main.getGuildID()).getMember(event.getAuthor());


        for (Guild.Ban bans : event.getJDA().getGuildById(main.getGuildID()).getBanList().complete()) {
            if (bans.getUser().getIdLong() == member.getUser().getIdLong())
                return;
        }

        for (TextChannel channel : event.getJDA().getCategoryById(main.getConfig().getConfigValue("supportCategoryId").getAsLong()).getTextChannels()) {
            if (channel.getName().startsWith(member.getEffectiveName())) {
                userCount++;
                if (userCount == 1) {
                    member.getUser().openPrivateChannel().complete().sendMessage("No channel has been created because you already have a ticket open! Please respond within the ticket to resolve that issue first!").queue();
                    return;
                }
            }
        }

        String[] userMessageSplit = userMessage.split(" ");

        boolean clean = false;

        for (int x = 0; x != userMessageSplit.length; x++) {
            for (JsonElement swearWords : main.getConfig().getConfigValue("swearWords").getAsJsonArray()) {
                if (!swearWords.getAsString().equalsIgnoreCase(userMessageSplit[x])) {
                    clean = true;
                }
                else {
                    clean = false;
                }
            }
        }

        if (clean) {
            TextChannel supportChannel = (TextChannel) event.getJDA().getCategoryById(main.getConfig().getConfigValue("supportCategoryId").getAsLong())
                    .createTextChannel(member.getEffectiveName() + "-" + ThreadLocalRandom.current().nextInt(99999)).complete();

            EmbedBuilder message = new EmbedBuilder()
                    .addField("Author: ", member.getAsMention(), true)
                    .addField("Ticket: ", userMessage, true)
                    .setDescription(userMessage)
                    .setFooter("If you are finished with this ticket, please click \u2705. All staff and developers can close the ticket also", event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                    .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()));
            Message supportMessage = main.getMessenger().sendEmbed(supportChannel, message.build()).getMessage();

            supportChannel.getManager().setTopic("Creation date: " + supportChannel.getCreationTime().format(dateFormat) + " Creation Time: " + supportChannel.getCreationTime().format(timeFormat) + " GMT Authors ID: " + event.getAuthor().getIdLong() + " Message ID: " + supportMessage.getIdLong() + " Channel ID: " + supportChannel.getIdLong()).queue();
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
                            main.getMessenger().sendMessage((TextChannel) event.getChannel(), event.getMessage().getAuthor() + " has sent a file called " + attachment.getFileName() + ".log");
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
            supportMessage.pin().complete();
            supportChannel.getHistory().retrievePast(1).queue(l -> l.forEach(m -> m.delete().queue()));
            supportMessage.addReaction("\u2705").queue();
            event.getAuthor().openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                    .setTitle("Support Channel")
                    .setDescription("https://discordapp.com/channels/" + main.getGuildID() + "/" + supportChannel.getIdLong())
                    .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()))
                    .build()).queue();
        } else {
            EmbedBuilder message = new EmbedBuilder()
                    .setTitle("Error!")
                    .setDescription("Cannot create a support ticket with swear words. Send the command again in the Discord to create a new ticket. Don't cuss this time.")
                    .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()));
            event.getAuthor().openPrivateChannel().complete().sendMessage(message.build()).queue();
        }
    }
}