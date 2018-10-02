package demo;

import redis.clients.jedis.Jedis;
import utils.RedisConnectionFactory;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Class RedisTest
 * @Description: TODO
 * @Author: luozhen
 * @Create: 2018/09/27 13:26
 */
public class RedisTest {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                Jedis jedis = RedisConnectionFactory.getConnection();
                for (int j = 0; j <= 3; j++) {
                    jedis.set(Thread.currentThread().getName() + " + " + j, Thread.currentThread().getName() + " + " + j);
                }
            });
        }
        executorService.shutdown();
        Set<String> keySet = RedisConnectionFactory.getConnection().keys("*");
        keySet.forEach(System.out::println);
    }
}
