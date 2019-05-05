package com.github.yourmcgeek.shadowrewrite.listeners;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import com.google.gson.JsonElement;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class PrivateMessageListenerNew extends ListenerAdapter {

    private ShadowRewrite main;
    private int userCount;
    private JSONObject data;
    private HashMap<Long, Message> userIDMessage = new HashMap<>();

    public PrivateMessageListenerNew(ShadowRewrite main) {
        this.main = main;
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/YY");
        if (event.getAuthor().isBot()) return;
        if (userIDMessage.isEmpty()) {
            Message username = event.getMessage(); // Getting Username and adding to Hashmap
            userIDMessage.put(event.getAuthor().getIdLong(), username);
        }
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

        String userMessage = null;
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Support Ticket Creation")
                .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()))
                .setDescription("To create a ticket, please respond here and a channel will be created." +
                        "\nNote: Multiple messages will not be combined, so please type only one message.\n" +
                        "Also, if you upload a file, the file will be taken and sent in the support channel also!");
        main.getMessenger().sendEmbed(event.getChannel(), embedBuilder.build());

        userMessage = event.getMessage().getContentRaw();
        if (!userIDMessage.isEmpty()) {
            if (!isValidUsername(userIDMessage.get(event.getMessage().getAuthor().getIdLong()).getContentRaw())) {
                main.getMessenger().sendEmbed(event.getChannel(), new EmbedBuilder()
                        .setDescription("Username cannot be found, please make sure the username in question is spelled correctly. If you are positive it's spelled correctly, please contact YourMCGeek.\nPlease enter your Username again.")
                        .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt())).setTitle("Error!").build(), 10);
                userIDMessage.clear();
            } else {
                TextChannel supportChannel = (TextChannel) event.getJDA().getCategoryById(main.getConfig().getConfigValue("supportCategoryId").getAsLong())
                        .createTextChannel(member.getEffectiveName() + "-" + ThreadLocalRandom.current().nextInt(99999)).complete();

                String regex = "(.{8})(.{4})(.{4})(.{4})(.{12})";
                String uuid = data.getString("id");
                String formattedUUID = uuid.replaceAll(regex, "$1-$2-$3-$4-$5");
                System.out.println(userIDMessage.get(event.getAuthor().getIdLong()).getContentRaw());
                EmbedBuilder message = new EmbedBuilder()
                        .addField("Author: ", member.getAsMention(), true)
                        .addField("Ticket: ", userMessage, true)
                        .addField("Username: ", data.getString("name"), true)
                        .addField("UUID: ", formattedUUID, true)
                        .setFooter("If you are finished, please click \u2705. All staff and developers can close the ticket also.", event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                        .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()));
                Message supportMessage = main.getMessenger().sendEmbed(supportChannel, message.build());

                supportChannel.getManager().setTopic("Creation date: " + supportChannel.getCreationTime().format(dateFormat) + " Authors ID: " + event.getAuthor().getIdLong() + " Message ID: " + supportMessage.getIdLong() + " Channel ID: " + supportChannel.getIdLong()).queue();
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
                        supportMessage.pin().complete();
                        supportChannel.getHistory().retrievePast(1).queue(l -> l.forEach(m -> m.delete().queue()));
                        supportMessage.addReaction("\u2705").queue();
                        event.getAuthor().openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                                .setTitle("Support Channel")
                                .setDescription("https://discordapp.com/channels/" + main.getGuildID() + "/" + supportChannel.getIdLong())
                                .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()))
                                .build()).queue();
                    }
                }
                userIDMessage.clear();
            }
        }
    }

    private boolean isValidUsername(String username) {
        int responseCode = 0;
        try {
            String url = "https://api.mojang.com/users/profiles/minecraft/" + username;
            URL obj = new URL(url);

            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            data = new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (responseCode == 204 || responseCode == 400)
            return false;
        else {
            return true;
        }
    }
}
