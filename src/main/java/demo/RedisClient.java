package demo;

import redis.clients.jedis.Jedis;
import utils.RedisConnectionFactory;

import java.util.Iterator;
import java.util.Set;

/**
 * @Class RedisClient
 * @Description: TODO
 * @Author: luozhen
 * @Create: 2018/09/22 12:58
 */
public class RedisClient {

    public static void main(String[] args) {
        Jedis jedis = RedisConnectionFactory.getConnection();
        for (int i = 1; i < 10; i++) {
            jedis.set("key" + i, "redis" + i);
            jedis.expire("key" + i, 60);
        }
        for (int i = 0; i < 10; i++) {
            Set<String> keySet = jedis.keys("*");
            Iterator iterator = keySet.iterator();
            if (keySet.size() > 0) {
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    System.out.print(key + ", ");
                }
                System.out.println("----------");
            } else {
                System.out.println("no keys");
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        RedisConnectionFactory.closeConnection();
        RedisConnectionFactory.closePool();
    }

}
