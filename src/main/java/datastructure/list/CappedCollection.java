package datastructure.list;

import redis.clients.jedis.Jedis;
import utils.JsonUtil;
import utils.RedisConnectionFactory;

/**
 * @Class CappedCollection
 * @Description: TODO
 * @Author: luozhen
 * @Create: 2018/09/27 22:51
 */
public class CappedCollection {

    private final int size;
    private String key;
    private final Jedis jedis = RedisConnectionFactory.getConnection();

    public CappedCollection(int size, String key) {
        this.size = size;
        this.key = key;
    }

    public void put(Object object) {
        jedis.lpush(this.key, JsonUtil.ObjectToJson(object));
        if (jedis.llen(this.key) > size) {
            trim();
        }
    }

    public Object get() {
        if (jedis.llen(this.key) == 0) {
            return null;
        }
        return JsonUtil.JsonToObject(jedis.rpop(this.key), Object.class);
    }

    private void trim() {
        jedis.ltrim(key, 0, size - 1);
    }

}
