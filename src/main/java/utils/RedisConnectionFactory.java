package utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Class RedisConnectionFactory
 * @Description: 获取jedis连接
 * @Author: luozhen
 * @Create: 2018/09/22 13:14
 */
public class RedisConnectionFactory {

    private static JedisPool jedisPool = null;
    private static String redisConfigFile;

    static {
        redisConfigFile = "redis.properties";
    }

    //把redis连接对象放到本地线程中
    private static ThreadLocal<Jedis> local=new ThreadLocal<>();

    private RedisConnectionFactory() {

    }

    private static void initialPool() {
        try {
            Properties properties = new Properties();
            InputStream in = RedisConnectionFactory.class.getClassLoader().getResourceAsStream(redisConfigFile);
            properties.load(in);
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(Integer.valueOf(properties.getProperty("jedis.pool.maxActive")));
            config.setMaxIdle(Integer.valueOf(properties.getProperty("jedis.pool.maxIdle")));
            config.setMaxWaitMillis(Integer.valueOf(properties.getProperty("jedis.pool.maxWait")));
            config.setTestOnBorrow(Boolean.valueOf(properties.getProperty("jedis.pool.testOnBorrow")));
            config.setTestOnReturn(Boolean.valueOf(properties.getProperty("jedis.pool.testOnReturn")));

            jedisPool = new JedisPool(config, properties.getProperty("redis.ip"),
                                                Integer.valueOf(properties.getProperty("redis.port")),
                                                Integer.valueOf(properties.getProperty("redis.timeout")),
                                                properties.getProperty("redis.password"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Jedis getConnection() {
        Jedis jedis = local.get();
        if (jedis == null) {
            if (jedisPool == null) {
                initialPool();
            }
            jedis = jedisPool.getResource();
            local.set(jedis);
        }
        return jedis;
    }

    public static void closeConnection() {
        Jedis jedis = local.get();
        if (jedis != null) {
            jedis.close();
        }
        local.set(null);
    }

    public static void closePool() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }

}
