package com.github.yourmcgeek.objects.remind;

import com.github.yourmcgeek.ShadowRewrite;
import com.github.yourmcgeek.objects.Datasource;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reminders extends Datasource {

    private ShadowRewrite main;

    public Reminders(ShadowRewrite main) {
        this.main = main;
        this.filename = "discordbot.reminders";
        this.size = 4;
        this.generateKey = item -> item[USERID] + "|" + item[CHANNELID] + "|" + item[EXPIRETIME];
    }

    public List<String[]> getRemindersForUser(String userId) {
        ArrayList<String[]> list = new ArrayList<>();
        synchronized (data) {
            data.values().stream().filter((item) -> (item[USERID].equals(userId))).forEach((item) -> {
                list.add(item.clone());
            });
        }
        return list;
    }

    public List<String[]> getExpiredReminders() {
        long now = OffsetDateTime.now().toInstant().toEpochMilli();
        ArrayList<String[]> list = new ArrayList<>();
        synchronized (data) {
            for (String[] item : data.values())
                if (now > Long.parseLong(item[EXPIRETIME]))
                    list.add(item.clone());
        }
        return list;
    }

    public void removeReminder(String[] reminder) {
        remove(generateKey.apply(reminder));
    }

    public void checkReminders(JDA jda) {
        if (jda.getStatus() != JDA.Status.CONNECTED)
            return;
        List<String[]> list = getExpiredReminders();
        list.stream().map((item) -> {
            removeReminder(item);
            return item;
        }).forEach((item) -> {
            TextChannel chan = jda.getTextChannelById(item[Reminders.CHANNELID]);
            if (chan == null) {
                User user = jda.getUserById(item[Reminders.USERID]);
                if (user != null)
                    main.getMessenger().sendPrivateMessage(user, "\u23F0 " + item[Reminders.MESSAGE]);
            } else {
                main.getMessenger().sendMessage(chan, "\u23F0 <@" + item[Reminders.USERID] + "> \u23F0 " + item[Reminders.MESSAGE]);
            }
        });
    }

    final public static int USERID = 0;
    final public static int CHANNELID = 1;
    final public static int EXPIRETIME = 2;
    final public static int MESSAGE = 3;
}
