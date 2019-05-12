package com.github.yourmcgeek.shadowrewrite.commands.remind;

import com.github.yourmcgeek.shadowrewrite.EmbedTemplates;
import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import com.github.yourmcgeek.shadowrewrite.pagination.Page;
import com.github.yourmcgeek.shadowrewrite.pagination.PageBuilder;
import com.github.yourmcgeek.shadowrewrite.pagination.PaginationEmbed;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Command(value = "remove", hideInHelp = true)
public class Remove {

    private ShadowRewrite main;

    @Execute
    public CommandResult onRemove(Member member, TextChannel channel, Message message, String label, List<String> args, ShadowRewrite main) {
        ArrayList<Reminder> reminders = main.getSqlManager().getRemindersForUser(member.getUser().getIdLong());
        if (main.getSqlManager().getRemindersForUser(member.getUser().getIdLong()).size() == 0) {
            EmbedTemplates.NO_REMINDS.getEmbed().build();
            return CommandResult.success();
        }
        int page = 1;
        int count = 1;
        int maxPages = main.getSqlManager().getRemindersForUser(member.getUser().getIdLong()).size();
        List<Page> content = new ArrayList<>();
        PaginationEmbed.Builder builder = new PaginationEmbed.Builder(member.getJDA());
        while (count <= maxPages) {
            content.add(generatePage(page, maxPages, count, reminders.stream(), member));
            if (page < maxPages)
                page++;
            count++;
        }
        content.forEach(page1 -> builder.addPage(page1));
        builder.buildAndDisplay(channel);
        return CommandResult.success();
    }

    public Page generatePage(int page, int maxPages, int count, Stream<Reminder> reminderStream, Member sender) {
        PageBuilder pageBuilder = new PageBuilder()
                .includeTimestamp(true)
                .setColor(Color.CYAN)
                .setTitle("__**Active Reminds**__");
        pageBuilder.setFooter("Page " + page + " of " + maxPages, sender.getJDA().getSelfUser().getAvatarUrl());
        reminderStream.filter(reminder -> reminder.getUserID() == sender.getUser().getIdLong()).forEach(reminder -> {
            long secs = 0;
            if (Instant.ofEpochMilli(reminder.getExpiry()).isAfter(Instant.now())) {
                long duration = Instant.ofEpochMilli(reminder.getExpiry()).minusMillis(Instant.now().toEpochMilli()).toEpochMilli();
                secs = TimeUnit.MILLISECONDS.toSeconds(duration);
            }
            String message = reminder.getMessage();
            List<String> lines = new ArrayList<>();

            lines.add(("\u2022\u0020 Expires in " + secs + "s"));

            pageBuilder.addContent(false, count + ". " + message, lines.toArray(new String[0]));
        });
        return pageBuilder.build();
    }
}