package pipeline;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import utils.RedisConnectionFactory;

/**
 * @Class PipelineExample
 * @Description: TODO
 * @Author: luozhen
 * @Create: 2018/09/29 13:54
 */
public class PipelineExample {

    public static void main(String[] args) {
        Jedis jedis = RedisConnectionFactory.getConnection();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            jedis.hset("hashKey:" + i, "field" + i, "value" + i);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("no pipeline spend : " + (endTime - startTime) / 1000 + "ms");
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Pipeline pipeline = jedis.pipelined();
            for (int j = i * 1000; j < (i + 1) * 1000; j++) {
                pipeline.hset("hashKey:" + j, "field" + j, "value" + j);
            }
            pipeline.syncAndReturnAll();
        }
        endTime = System.currentTimeMillis();
        System.out.println("pipeline spend : " + (endTime - startTime) / 1000 + "ms");
    }

}
