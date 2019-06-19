package com.github.yourmcgeek.shadowrewrite.listeners.redis;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.bhop.bjdautilities.ReactionMenu;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolAbstract;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;

public class TicketsListener {

    private final ShadowRewrite plugin;
    private final Gson gson = new Gson();
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/YY");
    private final ExecutorService executorService = new ScheduledThreadPoolExecutor(2);

    private Jedis jedis = null;
    private Subscription sub = null;

    public TicketsListener(ShadowRewrite plugin) {
        this.plugin = plugin;
    }

    public void register() {
        cleanup();
        executorService.submit(() -> {
            Optional<JedisPoolAbstract> pool = plugin.getClient().getPool();
            while (pool.isPresent()) {
                try {
                    sub = new Subscription();
                    jedis = pool.get().getResource();
                    jedis.subscribe(sub, "shadowintegration:ticket");
                } catch (JedisConnectionException ex) {
                    cleanup();
                } finally {
                    cleanup();
                }
            }
        });
    }

    private void cleanup() {
        if (sub != null) {
            sub.unsubscribe();
        }
        if (jedis != null) {
            jedis.close();
        }
    }

    private class Subscription extends JedisPubSub {

        @Override
        public void onMessage(String channel, String request) {
            if (!channel.equals("shadowintegration:ticket")) {
                return;
            }
            JsonObject obj = gson.fromJson(request, JsonObject.class);
            if (obj != null) {
                if (obj.get("type").getAsString().equalsIgnoreCase("creation")) {
                    String playerName = obj.get("playerName").getAsString();
                    for (TextChannel textChannel : plugin.getJDA().getGuildById(plugin.getGuildID()).getCategoryById(plugin.getConfig().getConfigValue("supportCategoryId").getAsLong()).getTextChannels()) {
                        if (textChannel.getName().toLowerCase().startsWith(playerName.toLowerCase()))
                            return;
                    }

                    TextChannel supportChannel = (TextChannel) plugin.getJDA().getCategoryById(plugin.getConfig().getConfigValue("supportCategoryId").getAsLong())
                            .createTextChannel(playerName + "-" + ThreadLocalRandom.current().nextInt(99999)).complete();

                    EmbedBuilder message = new EmbedBuilder()
                            .addField("Author: ", playerName, true)
                            .addField("Ticket: ", obj.get("message").getAsString(), true)
                            .addField("Username: ", playerName, true)
                            .addField("UUID: ", ("[" + obj.get("uuid").getAsString() + "](https://mcuuid.net/?q=" + obj.get("uuid").getAsString() + ")"), true)
                            .addField("Server:", obj.get("serverName").getAsString(), true)
                            .setFooter("If you are finished, please click \u2705. All staff and developers can close the ticket also.", plugin.getJDA().getSelfUser().getEffectiveAvatarUrl())
                            .setColor(new Color(plugin.getConfig().getConfigValue("Red").getAsInt(), plugin.getConfig().getConfigValue("Blue").getAsInt(), plugin.getConfig().getConfigValue("Green").getAsInt()));

                    ReactionMenu supportMessage = new ReactionMenu.Builder(plugin.getJDA()).setEmbed(message.build()).setStartingReactions("\uD83D\uDD12", "\u2705").setRemoveReactions(false).buildAndDisplay(supportChannel);
                    supportChannel.getManager().setTopic("Creation date: " + supportChannel.getCreationTime().format(dateFormat) + " Authors ID: " + obj.get("uuid").getAsString() + " Message ID: " + supportMessage.getMessage().getIdLong() + " Channel ID: " + supportChannel.getIdLong()).queue();

                    JsonObject success = new JsonObject();
                    success.addProperty("type", "success");
                    success.addProperty("uuid", obj.get("uuid").getAsString());
                    success.addProperty("link", "https://discordapp.com/channels/" + plugin.getGuildID() + "/" + supportChannel.getIdLong());

                    jedis.publish("shadowintegration:ticket", success.toString());
                }
            }
        }
    }
}
