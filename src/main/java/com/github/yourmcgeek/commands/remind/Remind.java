package com.github.yourmcgeek.commands.remind;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Remind extends Command {

    private ShadowRewrite main;
    public Remind(ShadowRewrite main) {
        this.main = main;
        this.name = "remind";
        this.help = "Sets a reminder with a custom time and message!";
    }

    @Override
    protected void execute(CommandEvent event) {

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
            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                    .setTitle("Reminder")
                    .setDescription(content);
            event.getMember().getUser().openPrivateChannel().complete().sendMessage(embed.build()).completeAfter(timeAmt, TimeUnit.MILLISECONDS);
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