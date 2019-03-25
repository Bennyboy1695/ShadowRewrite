package com.github.yourmcgeek.commands.remind;

import com.github.yourmcgeek.ShadowRewrite;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.bhop.bjdautilities.command.CommandResult;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.List;

@Command(label = {"remind", "reminder", "remindme"}, usage = "remind {time}{s|m|h|d} {Reminder}", description = "Sets a reminder with a custom time and message!", minArgs = 2)
public class Remind  {

    private ShadowRewrite main;
    public Remind(ShadowRewrite main) {
        this.main = main;
    }

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            int timeAmt;
            String timeUnit;

            timeAmt = Integer.parseInt(args.get(0).substring(0, (args.get(0).length() - 1)));
            timeUnit = args.get(0).substring(args.get(0).length() - 1);

            switch (timeUnit) {
                case "s": // Seconds
                    timeAmt = timeAmt * 1000;
                    break;
                case "m": // Minutes
                    timeAmt = timeAmt * 1000 * 60;
                    break;
                case "h": // Hours
                    timeAmt = timeAmt * 1000 * 60 * 60;
                    break;
                case "d": // Days
                    timeAmt = timeAmt * 1000 * 60 * 60 * 24;
                    break;
                default: // Minutes
                    timeAmt = timeAmt * 1000 * 60;
                    break;
            }


            main.getLogger().info(args.toArray().toString());
            args.remove(0);
            String content = String.join(",", args);
            content = content.replaceAll(",", " ");
            main.getLogger().info(content);

            EmbedBuilder confirm = new EmbedBuilder()
                    .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                    .setTitle("Please Confirm")
                    .setAuthor(String.valueOf(message.getAuthor()), String.valueOf(message.getAuthor().getAsTag()), String.valueOf(message.getAuthor().getEffectiveAvatarUrl()))
                    .addField("Remind in", timeAmt + timeUnit, true)
                    .addField("Reminder", content, true)
                    .setDescription("Please confirm by reacting with a \u2705 within 15 seconds");
            long messageID = main.getMessenger().sendEmbed(channel, confirm.build(), 15).getId();
            long userID = message.getAuthor().getIdLong();

            try {
                JsonObject object = new JsonObject();
                object.addProperty("userID", userID);
                object.addProperty("messageID", messageID);
                object.addProperty("time", timeAmt);
                object.addProperty("unit", timeUnit);
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
            main.getMessenger().sendEmbed(channel, embed.build(), 10);
        }
        return CommandResult.SUCCESS;
    }
}