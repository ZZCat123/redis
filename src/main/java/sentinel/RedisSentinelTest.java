package sentinel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import utils.RedisConnectionFactory;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Class RedisSentinelTest
 * @Description: TODO
 * @Author: luozhen
 * @Create: 2018/10/04 14:48
 */
public class RedisSentinelTest {

    private static Logger logger = LoggerFactory.getLogger(RedisSentinelTest.class);

    public static void main(String[] args) {
        String masterName = "mymaster";
        Set<String> sentinels = new HashSet<>();
        sentinels.add("127.0.0.1:26379");
        sentinels.add("127.0.0.1:26380");
        sentinels.add("127.0.0.1:26381");
        while (true) {
            Jedis jedis = null;
            try {
                jedis = RedisConnectionFactory.getConnectionFromSentinelPool(masterName, sentinels, 1000);
                int index = new Random().nextInt(100000);
                String key = "k-" + index;
                String value = "v-" + index;
                jedis.set(key, value);
                logger.info("{} value is {}", key, jedis.get(key));
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }
    }

}
