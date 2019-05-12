package com.github.yourmcgeek.shadowrewrite.commands.remind;

import com.github.yourmcgeek.shadowrewrite.EmbedTemplates;
import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import me.bhop.bjdautilities.ReactionMenu;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Command(value = "remove", hideInHelp = true, usage = "remind remove 1")
public class Remove {

    private ShadowRewrite main;

    @Execute
    public CommandResult onRemove(Member member, TextChannel channel, Message message, String label, List<String> args, ShadowRewrite main) {
        List<Reminder> remindersList = new ArrayList();
        int count = 1;
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()))
                .setTitle("Active Reminders");
        for (Reminder reminder : main.getSqlManager().getRemindersForUser(member.getUser().getIdLong())) {
            remindersList.add(reminder);
            builder.appendDescription(count + ". " + reminder.getMessage() + "\n");
            count++;
        }
        main.getLogger().info(remindersList.toString());
        if (args.isEmpty()) {
            ReactionMenu reactionMenu = new ReactionMenu.Builder(member.getJDA())
                    .addStartingReaction("\u274C")
                    .setEmbed(builder.build())
                    .onClick("\u274C", menu -> menu.destroy()).buildAndDisplay(channel);

            main.getMessenger().sendEmbed(channel, EmbedTemplates.ERROR.getEmbed().setDescription("Invalid Arguments, run the command with the remind number you'd like to remove.").build(), 10);

            return CommandResult.invalidArguments();
        }

        main.getLogger().info("Removing Reminder: " + remindersList.get(Integer.valueOf(args.get(0)) - 1).getMessage() + "\n" + remindersList.get(Integer.valueOf(args.get(0)) - 1).getCreation());
        main.getSqlManager().removeRemind(remindersList.get(Integer.valueOf(args.get(0)) - 1));

        return CommandResult.success();
    }
}