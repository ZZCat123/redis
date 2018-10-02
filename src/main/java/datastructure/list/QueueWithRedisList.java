package datastructure.list;

import redis.clients.jedis.Jedis;
import utils.JsonUtil;
import utils.RedisConnectionFactory;

/**
 * @Class QueueWithRedisList
 * @Description: TODO
 * @Author: luozhen
 * @Create: 2018/09/27 22:44
 */
public class QueueWithRedisList {

    private final Jedis jedis = RedisConnectionFactory.getConnection();

    private String key;

    public QueueWithRedisList(String key) {
        this.key = key;
    }

    public void put(Object object) {
        jedis.lpush(this.key, JsonUtil.ObjectToJson(object));
    }

    public Object get() {
        if (jedis.llen(key) == 0) {
            return null;
        }
        return JsonUtil.JsonToObject(jedis.rpop(this.key), Object.class);
    }

    public static void main(String[] args) {
        QueueWithRedisList queue = new QueueWithRedisList("Queue");
        for (int i = 0; i < 10; i++) {
            queue.put("queue " + i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(queue.get().toString());
        }
    }

}
