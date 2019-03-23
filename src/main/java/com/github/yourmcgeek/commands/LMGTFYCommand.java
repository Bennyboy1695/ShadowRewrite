package com.github.yourmcgeek.commands;

import com.github.yourmcgeek.ShadowRewrite;
import me.bhop.bjdautilities.command.CommandResult;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.util.List;

@Command(label = {"google", "lmgtfy"}, usage = "google {args}", description = "Displays a LMGTFY link", minArgs = 1, permission = Permission.ADMINISTRATOR)
public class LMGTFYCommand {

    private ShadowRewrite main;

    public LMGTFYCommand(ShadowRewrite main) {
        this.main = main;
    }

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args) {
        message.delete().complete();

        String link = "";
        StringBuilder link2 = new StringBuilder();
        for (int x = 0; x != args.size(); x++) {
            if (x == args.size() - 1) {
                link2.append(args.get(x));
            }
            else {
                link2.append(args.get(x) + "+");
            }
        }

        String BASE_URL = "http://lmgtfy.com/?q=";
        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Search Result")
                .setColor(new Color(main.mgr.getConfig().getColorRed(), main.mgr.getConfig().getColorGreen(), main.mgr.getConfig().getColorBlue()))
                .setDescription(BASE_URL + link2.toString());
        main.getMessenger().sendEmbed(channel, embed.build(), 10);
        return CommandResult.SUCCESS;
    }
}
