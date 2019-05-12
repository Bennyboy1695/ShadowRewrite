package com.github.yourmcgeek.shadowrewrite.commands.remind;

import com.github.yourmcgeek.shadowrewrite.EmbedTemplates;
import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import com.github.yourmcgeek.shadowrewrite.utils.Util;
import me.bhop.bjdautilities.ReactionMenu;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Command(label = {"add", "create"}, minArgs = 2, usage = "remind add 1d1h1m1s", hideInHelp = true)
public class Add {
    @Execute
    public CommandResult onAdd(Member member, TextChannel channel, Message message, String label, List<String> args, ShadowRewrite main) {
        String length = args.get(0);
        if (length.toLowerCase().matches("([0-9]+w)?([0-9]+d)?([0-9]+h)?([0-9]+m)?([0-9]+s)?")) {
            List<String> mine = args;
            mine.remove(0);
            String msg = String.join(",", mine).replaceAll(",", " ");
            long expiry = Util.stringToMillisConverter(length);
            long finalExpiry = Instant.now().plusMillis(expiry).toEpochMilli();

            Reminder reminder = new Reminder(member.getUser().getIdLong(), channel.getIdLong(), Instant.now().toEpochMilli(), finalExpiry, msg);
            try {
                main.getSqlManager().addNewRemind(reminder);
                main.getMessenger().sendEmbed(channel, EmbedTemplates.SUCCESS.getEmbed().setTitle("Success").setDescription("Successfully added a reminder that will appear in " + length).addField("Message: ", msg, true).build(), 10);
            } catch (Exception e) {
                e.printStackTrace();
            }
            long duration = TimeUnit.MILLISECONDS.toSeconds(expiry);

            ScheduledExecutorService remindCreation = (ScheduledExecutorService) main.getExecutors().schedule(() -> {
                final Message tagMessage = channel.sendMessage(member.getAsMention() + " here is the reminder you asked for!").complete();
                new ReactionMenu.Builder(main.getJDA())
                        .setEmbed(EmbedTemplates.PRETTY_SUCCESSFULL.getEmbed().setTitle("Reminder").addField("Message: ", msg, true).setFooter("To delete this message react with a \u274C!", main.getJDA().getSelfUser().getAvatarUrl()).build())
                        .setRemoveReactions(true)
                        .onClick("\u274C", (reactionMenu, reactor) -> {
                            if (reactor.equals(member)) {
                                reactionMenu.destroy();
                            } else if (reactor.hasPermission(Permission.MESSAGE_MANAGE)) {
                                reactionMenu.destroy();
                            }
                        })
                        .onDestroy(delete -> tagMessage.delete().complete())
                        .buildAndDisplay(channel);
                main.getSqlManager().removeRemind(reminder);
            }, duration, TimeUnit.SECONDS);
            main.getScheduledTasks().add(remindCreation);
        return CommandResult.success();
        }
        else {
            return CommandResult.invalidArguments();
        }
    }
}