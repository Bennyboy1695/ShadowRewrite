package com.github.yourmcgeek.shadowrewrite.listeners.redis;

import com.github.yourmcgeek.shadowrewrite.ShadowRewrite;
import redis.clients.jedis.JedisPoolAbstract;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public final class RedisClient {

    private final ShadowRewrite plugin;

    private String host;
    private String password;
    private String poolName;

    private List<String> sentinels;

    private Integer port;
    private Integer database;
    private Integer timeout;

    private JedisPoolAbstract jedisPool = null;

    public RedisClient(ShadowRewrite plugin) {
        this.plugin = plugin;
    }

    public boolean load() {
        plugin.getLogger().info("[Sponge] Connecting to redis.");

        this.host = plugin.getMainConfig().getConfigValue("redis", "hostname").getAsString();
        this.password = plugin.getMainConfig().getConfigValue("redis", "password").getAsString();
        this.poolName = plugin.getMainConfig().getConfigValue("redis", "poolName").getAsString();

        this.sentinels = plugin.getMainConfig().getConfigValue("redis", "sentinels");

        this.port = plugin.getMainConfig().getConfigValue("redis", "port").getAsInt();
        this.database = plugin.getMainConfig().getConfigValue("redis", "database").getAsInt();

        if (this.password == null || this.password.equals("")) {
            plugin.getLogger().error("Password not specified unable to connect to redis!");
            return false;
        }

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setBlockWhenExhausted(false);

        jedisPool = new JedisSentinelPool(poolName, new HashSet<>(sentinels), poolConfig, 1000, password, database);
        return true;
    }

    public boolean destroy() {
        if (jedisPool != null) {
            jedisPool.destroy();
            return true;
        }
        return false;
    }

    public Optional<JedisPoolAbstract> getPool() {
        if (jedisPool == null || jedisPool.isClosed()) {
            return Optional.empty();
        }
        return Optional.of(jedisPool);
    }
}
