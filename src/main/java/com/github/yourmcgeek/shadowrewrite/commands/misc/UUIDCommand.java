package com.github.yourmcgeek.shadowrewrite.commands.misc;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import me.bhop.bjdautilities.command.annotation.Command;
import me.bhop.bjdautilities.command.annotation.Execute;
import me.bhop.bjdautilities.command.result.CommandResult;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Member;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Command(label = "uuid", minArgs = 1, description = "Gets the UUID for the entered username", usage = "uuid YourMCGeek")
public class UUIDCommand {

    @Execute
    public CommandResult onExecute(Member member, TextChannel channel, Message message, String label, List<String> args, ShadowRewrite main) {
        String username = args.get(0);
        try {
            String url = "https://api.mojang.com/users/profiles/minecraft/" + username;
            URL obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject data = new JSONObject(response.toString());
            if (responseCode == 204) {
                main.getMessenger().sendEmbed(channel, new EmbedBuilder()
                        .setDescription("Username cannot be found, please make sure the username in question is spelled correctly.")
                        .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()))
                        .setTitle("Error!").build(), 10);
            }
            if (responseCode == 400) {
                main.getMessenger().sendEmbed(channel, new EmbedBuilder()
                        .setDescription(data.getString("errorMessage"))
                        .setTitle("Error!")
                        .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()))
                        .build(), 10);
            }
            String regex = "(.{8})(.{4})(.{4})(.{4})(.{12})";
            String uuid = data.getString("id");
            String formattedUUID = uuid.replaceAll(regex, "$1-$2");
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("UUID for " + data.getString("name"))
                    .addField("UUID", uuid, true)
                    .addField("UUID", formattedUUID, true)
                    .setColor(new Color(main.getConfig().getConfigValue("Red").getAsInt(), main.getConfig().getConfigValue("Blue").getAsInt(), main.getConfig().getConfigValue("Green").getAsInt()));
            main.getMessenger().sendEmbed(channel, embed.build(), 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommandResult.success();
    }
}