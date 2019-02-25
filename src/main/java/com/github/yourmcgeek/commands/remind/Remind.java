package com.github.yourmcgeek.commands.remind;

import com.github.yourmcgeek.ShadowRewrite;
import com.google.gson.*;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Remind extends Command {

    private ShadowRewrite main;
    public Remind(ShadowRewrite main) {
        this.main = main;
        this.name = "remind";
        this.help = "Sets a reminder with a custom time and message!";
    }

    @Override
    protected void execute(CommandEvent event) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            String[] args = event.getArgs().split(" ");

            event.getMessage().delete().complete();
            int timeAmt;
            String timeUnit;

            timeAmt = Integer.parseInt(args[0].substring(0, (args[0].length() - 1)));
            timeUnit = args[0].substring((args[0].length() - 1));

            switch (timeUnit) {
                case "s":
                    timeAmt = timeAmt * 1000;
                    break;
                case "m":
                    timeAmt = timeAmt * 1000 * 60;
                    break;
                case "h":
                    timeAmt = timeAmt * 1000 * 60 * 60;
                    break;
                case "d":
                    timeAmt = timeAmt * 1000 * 60 * 60 * 24;
                    break;
                default:
                    timeAmt = timeAmt * 1000 * 60;
                    break;
            }
            List<String> argsList = new LinkedList<>(Arrays.asList(args));

            System.out.println(argsList);
            argsList.remove(0);
            String content = String.join(",", argsList);
            content = content.replaceAll(",", " ");

            EmbedBuilder confirm = new EmbedBuilder()
                    .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                    .setTitle("Please Confirm")
                    .setAuthor(String.valueOf(event.getAuthor()), String.valueOf(event.getAuthor().getAsTag()), String.valueOf(event.getAuthor().getEffectiveAvatarUrl()))
                    .addField("Remind in", timeAmt + timeUnit, true)
                    .addField("Reminder", content, true)
                    .setDescription("Please confirm by reacting with a \u2705 within 15 seconds");
            long messageID = main.getMessenger().sendEmbed((TextChannel) event.getChannel(), confirm.build(), 15).getIdLong();
            long userID = event.getAuthor().getIdLong();

            try {
                JsonObject object = new JsonObject();
                object.addProperty("userID", userID);
                object.addProperty("messageID", messageID);
                object.addProperty("time", timeAmt);
                object.add("content", new JsonParser().parse(content).getAsJsonObject());
                main.getConfirmMessages().add(object);
            } catch (Exception e) {
                e.printStackTrace();
            }


            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                    .setTitle("Reminder")
                    .setDescription(content);
//            event.getMember().getUser().openPrivateChannel().complete().sendMessage(embed.build()).completeAfter(timeAmt, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                    .setTitle("Error!")
                    .addField("Time Units", "s = seconds\nm = minutes\nh = hours\nd = days", true)
                    .setDescription(String.format("Proper usage is %sremind {Duration}{TimeUnit} {Message}", main.mgr.getConfig().getPrefix()));
            main.getMessenger().sendEmbed((TextChannel) event.getChannel(), embed.build(), 10);
        }
    }
}