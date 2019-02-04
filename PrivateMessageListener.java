package com.github.yourmcgeek.listeners;

import com.github.yourmcgeek.ShadowRewrite;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
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
<<<<<<< HEAD
        System.out.println(main.mgr.getConfig().getGuildID());
        Member member = event.getJDA().getGuildById(main.getGuildID()).getMember(event.getAuthor());

=======
        System.out.println(main.mgr.getConfig().getGuildId());
        Member member = event.getJDA().getGuildById(main.getGuildId()).getMember(event.getAuthor());
>>>>>>> 62a18445caabb52b1ccf90ca3f80c9dc7e3e48ed
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
                .createTextChannel(event.getAuthor().getName() + "-" + ThreadLocalRandom.current().nextInt(99999)).complete();

//        supportChannel.createPermissionOverride(member).setAllow(101440).complete();
        supportChannel.getManager().setTopic("Creation date: "+ supportChannel.getCreationTime().format(dateFormat) + " Creation Time: " + supportChannel.getCreationTime().format(timeFormat) + "GMT").complete();
        Message message = new MessageBuilder()
                .append("**Author:** " + member.getAsMention())
                .append("\n")
                .append("**Message:** " + userMessage)
                .append("\n")
                .append("\n")
                .append("_To close this ticket please react with a \u2705 to this message!_")
                .build();
        Message supportMessage = main.getMessenger().sendMessage(supportChannel, message, 0);
        for (Message.Attachment attachment : event.getMessage().getAttachments()) {
            try {
                if (!new File(main.getLogDirectory().toFile(), "tmp").exists()) {
                    new File(main.getLogDirectory().toFile(), "tmp").mkdir();
                }
                attachment.download(new File(main.getLogDirectory().toFile() + "/tmp/", supportChannel.getName() + ".log"));
                supportChannel.sendFile(new File(main.getLogDirectory().toFile() + "/tmp/", supportChannel.getName() + ".log")).complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        supportMessage.pin().complete();
        supportChannel.getHistory().retrievePast(1).queue(l -> l.forEach(m -> m.delete().queue()));
        supportMessage.addReaction("\u2705").complete();
        event.getAuthor().openPrivateChannel().complete().sendMessage(new EmbedBuilder()
                .setTitle("Support Channel")
                .setDescription("https://discordapp.com/channels/" + main.getGuildId()  + "/" + supportChannel.getIdLong())
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorBlue(), main.mgr.getConfig().getColorGreen()))
                .build()).complete();
    }
}
