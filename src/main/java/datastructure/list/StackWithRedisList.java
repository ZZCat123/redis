package datastructure.list;

import redis.clients.jedis.Jedis;
import utils.JsonUtil;
import utils.RedisConnectionFactory;

/**
 * @Class StackWithRedisList
 * @Description: TODO
 * @Author: luozhen
 * @Create: 2018/09/27 22:06
 */
public class StackWithRedisList {

    private String key;

    private Jedis jedis = RedisConnectionFactory.getConnection();

    public StackWithRedisList(String key) {
        this.key = key;
    }

    public void push(Object object) {
        jedis.lpush(this.key, JsonUtil.ObjectToJson(object));
    }

    public Object pop() {
        if (jedis.llen(this.key) == 0) {
            return null;
        }
        return JsonUtil.JsonToObject(jedis.lpop(key), Object.class);
    }

    public static void main(String[] args) {
        StackWithRedisList stack = new StackWithRedisList("StrStack");
        for (int i = 0; i < 10; i++) {
            stack.push("String " + i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(stack.pop());
        }
    }

}
