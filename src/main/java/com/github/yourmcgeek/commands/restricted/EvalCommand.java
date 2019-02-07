package com.github.yourmcgeek.commands.restricted;

import com.github.yourmcgeek.ShadowRewrite;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.TextChannel;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class EvalCommand extends Command {
    private ScriptEngine engine;
    private ShadowRewrite main;
    public EvalCommand(ShadowRewrite main) {
        this.main = main;
        this.name = "Eval";
        this.hidden = true;
        this.ownerCommand = true;
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval("var imports = new JavaImporter(" +
                    "java.io," +
                    "java.lang," +
                    "java.util," +
                    "Packages.net.dv8tion.jda.core" +
                    "Packages.net.dv8tion.jda.core.entities" +
                    "Packages.net.dv8tion.jda.core.impl" +
                    "Packages.net.dv8tion.jda.core.managers" +
                    "Packages.net.dv8tion.jda.core.managers.impl" +
                    "Packages.net.dv8tion.jda.core.utils);");
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
    protected void execute(CommandEvent event) {
        if (!event.getAuthor().getId().equals(main.mgr.getConfig().getBotOwnerId())) {
            main.getMessenger().sendMessage((TextChannel) event.getChannel(), "You cannot use this command as it's a restricted command!", 15);
        }
        else {
            try
            {
                engine.put("event", event);
                engine.put("message", event.getMessage());
                engine.put("channel", event.getChannel());
                engine.put("args", event.getArgs());
                engine.put("api", event.getJDA());
                if (event.isFromType(ChannelType.TEXT))
                {
                    engine.put("guild", event.getGuild());
                    engine.put("member", event.getMember());
                }

                Object out = engine.eval(
                        "(function() {" +
                                "with (imports) {" +
                                event.getMessage().getContentDisplay().substring(event.getArgs().length()) +
                                "}" +
                                "})();");
                event.reply(out == null ? "Executed without error." : out.toString());
            }
            catch (Exception e1)
            {
                event.reply(e1.getMessage());
            }
        }
    }
}