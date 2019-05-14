package com.github.yourmcgeek.shadowrewrite.listeners;

import com.github.yourmcgeek.shadowrewrite.EmbedTemplates;
import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
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
            if (channel.getName().startsWith(event.getAuthor().getName().toLowerCase())) {
                userCount++;
                if (userCount >= 2) {
                    event.getMessage().delete().queue();
                    main.getMessenger().sendEmbed(event.getChannel(), EmbedTemplates.ERROR.getEmbed().appendDescription("No channel has been created because you have multiple channels open already. Please complete these issue first!").build(), 10);
                    return;
                }
            }
        }

        String userMessage = event.getMessage().getContentRaw();
        event.getMessage().delete().queue();

        TextChannel supportChannel = (TextChannel) event.getJDA().getCategoryById(main.getConfig().getConfigValue("supportCategoryId").getAsLong())
                .createTextChannel(member.getEffectiveName() + "-" + ThreadLocalRandom.current().nextInt(99999)).complete();

        supportChannel.putPermissionOverride(member).setAllow(101440).complete();
        EmbedBuilder message = new EmbedBuilder()
                .addField("Author: ", member.getAsMention(), true)
                .addField("Ticket: ", userMessage, true)
                .addField("Username: ", "Run `" + main.getPrefix() + "username <Username>` to set this field", true)
                .addField("UUID: ", "Run `" + main.getPrefix() + "username <Username>` to set this field", true)
                .addField("Server:", "Run `" + main.getPrefix() + "server <Server>` to set this field", true)
                .setFooter("If you are finished, please click \u2705. All staff and developers can close the ticket also.", event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()));

        ReactionMenu supportMessage = new ReactionMenu.Builder(event.getJDA()).setEmbed(message.build()).setRemoveReactions(false).buildAndDisplay(supportChannel);
        supportChannel.getManager().setTopic("Creation date: " + supportChannel.getCreationTime().format(dateFormat) + " Authors ID: " + event.getAuthor().getIdLong() + " Message ID: " + supportMessage.getMessage().getIdLong() + " Channel ID: " + supportChannel.getIdLong()).queue();

        for (Message.Attachment attachment : event.getMessage().getAttachments()) {
            try {
                if (!new File(main.getLogDirectory().toFile(), "attachments").exists()) {
                    new File(main.getLogDirectory().toFile(), "attachments").mkdir();
                }
                attachment.download(new File(main.getLogDirectory().toFile() + "/attachments/", attachment.getFileName()));
                supportChannel.sendFile(new File(main.getAttachmentDir().toFile(), attachment.getFileName())).complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        supportMessage.addReaction("\u2705");
        supportMessage.addReaction("\uD83D\uDD12");
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Support Channel")
                .setDescription("If you would prefer this ticket to be private, or concerns a dupe, glitch, bug, or contribution payment please react in your ticket channel with \uD83D\uDD12 to only allow staff to view the ticket. ")
                .addField("Ticket", "https://discordapp.com/channels/" + main.getGuildID() + "/" + supportChannel.getIdLong(), false)
                .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()));
        main.getMessenger().sendEmbed(event.getChannel(), embedBuilder.build(), 10);
    }
}