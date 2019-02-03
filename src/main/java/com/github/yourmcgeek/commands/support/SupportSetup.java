package com.github.yourmcgeek.commands.support;



import com.github.yourmcgeek.objects.config.Config;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

public class SupportSetup extends Command {

    public SupportSetup() {
        this.name = "SetupSupport";
        this.aliases = new String[]{};
        this.help = "Setup Support Main Channel";
        this.hidden = true;
        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
    }

    @Override
    protected void execute(CommandEvent event) {
        Config config = new Config();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(config.discord.color);
        builder.setDescription("To make a support ticket please type your message in this channel that describes your issue fully. Please remember to always include the name of the server you are needing support on. When you do this a dedicated channel will be created for your issue. Once this is done Staff will contact you to try and resolve your issue.\n" +
                "\n" +
                "Tickets are not to be used for small questions that can be easily answered for these please use the <#379181828302700547> channel. Anything that requires in-depth staff interaction warrants creating a ticket. DO NOT abuse our ticket system doing so will result in a mute, kick or ban from our Discord.");
        event.reply(builder.build());
    }
}
