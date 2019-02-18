/*
 *
 * Original file from NetBans Support Bot. Used with permission, original source here
 * https://github.com/NetBans/SupportBot/
 *
 */

package com.github.yourmcgeek.listeners;

import com.github.yourmcgeek.ShadowRewrite;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class PrivateMessageListener extends ListenerAdapter {

    private ShadowRewrite main;
    private int userCount;
    public PrivateMessageListener(ShadowRewrite main) {
        this.main = main;
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/YY");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        if (event.getAuthor().isBot()) return;

        String userMessage = event.getMessage().getContentRaw();
        System.out.println(userMessage);

        System.out.println(main.mgr.getConfig().getGuildID());
        Member member = event.getJDA().getGuildById(main.getGuildID()).getMember(event.getAuthor());
		
        System.out.println(member);

        for (Guild.Ban bans : event.getJDA().getGuildById(main.getGuildId()).getBanList().complete()) {
            if (bans.getUser().getIdLong() == member.getUser().getIdLong())
                return;
        }

        for (TextChannel channel : event.getJDA().getCategoryById(Long.valueOf(main.mgr.getConfig().getSupportCategoryId())).getTextChannels()) {
            if (channel.getName().startsWith(event.getAuthor().getName())) {
                userCount++;
                if (userCount >= 3) {
                    member.getUser().openPrivateChannel().complete().sendMessage("No channel has been created because you multiple channels open already. Please complete these issue first!").complete();
                    return;
                }
            }
        }

        TextChannel supportChannel = (TextChannel) event.getJDA().getCategoryById(main.mgr.getConfig().getSupportCategoryId())
                .createTextChannel(member.getEffectiveName() + "-" + ThreadLocalRandom.current().nextInt(99999)).complete();

        supportChannel.getManager().setTopic(event.getAuthor().getIdLong() + " Creation date: "+ supportChannel.getCreationTime().format(dateFormat) + " Creation Time: " + supportChannel.getCreationTime().format(timeFormat) + "GMT").complete();

        EmbedBuilder message = new EmbedBuilder()
                .setDescription(member.getAsMention())
                .addField("Ticket: ", userMessage, false)
                .addField("Finished? ", "If you are finished with this ticket, please click \u2705. _All staff and developers can close the ticket also_", true)
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()));

        Message supportMessage = main.getMessenger().sendEmbed(supportChannel, message.build(), 0);
        for (Message.Attachment attachment : event.getMessage().getAttachments()) {
            String[] fileName = attachment.getFileName().split("\\.");
            if (main.mgr.getConfig().getBlacklistFiles().contains(fileName[1])) {
                try {
                    if (!new File(main.getLogDirectory().toFile(), "attachments").exists()) {
                        new File(main.getLogDirectory().toFile(), "attachments").mkdir();
                    }
                    attachment.download(new File(main.getLogDirectory().toFile() + "/attachments/", attachment.getFileName() + ".log"));
                    supportChannel.sendFile(new File(main.getLogDirectory().toFile() + "/attachments/", attachment.getFileName() + ".log")).complete();
                    main.getMessenger().sendMessage((TextChannel) event.getChannel(), event.getMessage().getAuthor() + " has sent a file called " + attachment.getFileName() + ".log");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                attachment.download(new File(main.getLogDirectory().toFile() + "/attachments/", attachment.getFileName()));
                supportChannel.sendFile(new File(main.getLogDirectory().toFile() + "/attachments/", attachment.getFileName())).complete();
                    main.getMessenger().sendMessage((TextChannel) event.getChannel(), event.getMessage().getAuthor() + " has sent a file called " + attachment.getFileName());
            }
        }
        supportMessage.pin().complete();
        supportChannel.getHistory().retrievePast(1).queue(l -> l.forEach(m -> m.delete().queue()));
        supportMessage.addReaction("\u2705").complete();
        event.getAuthor().openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                .setTitle("Support Channel")
                .setDescription("https://discordapp.com/channels/" + main.getGuildId()  + "/" + supportChannel.getIdLong())
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                .build()).complete();
    }
}
