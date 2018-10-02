package demo;

import entity.UserInfo;
import redis.clients.jedis.Jedis;
import utils.JsonUtil;
import utils.RedisConnectionFactory;

/**
 * @Class UserRedisService
 * @Description: TODO
 * @Author: luozhen
 * @Create: 2018/09/27 15:14
 */
public class UserRedisService {

    private String info = "userInfo";

    private String userName = "userName";

    private String userId = "userId";
    private String userAge = "userAge";
    private String userPhoneNumber = "userPhoneNumber";

    public int saveUserInfoInHash(Jedis jedis, UserInfo userInfo) {
        String temp = info + "saveUserInfoInHash" + userInfo.getUserId();
        jedis.hset(temp, userName, userInfo.getUserName());
        jedis.hset(temp, userId, String.valueOf(userInfo.getUserId()));
        jedis.hset(temp, userAge, String.valueOf(userInfo.getUserAge()));
        jedis.hset(temp, userPhoneNumber, userInfo.getUserPhoneNumber());
        return 1;
    }

    public UserInfo getUserInfoFromHash(Jedis jedis, int id) {
        UserInfo userInfo = new UserInfo();
        String temp = info + "saveUserInfoInHash" + id;
        userInfo.setUserName(jedis.hget(temp, userName));
        userInfo.setUserId(Integer.valueOf(jedis.hget(temp, userId)));
        userInfo.setUserAge(Integer.valueOf(jedis.hget(temp, userAge)));
        userInfo.setUserPhoneNumber(jedis.hget(temp, userPhoneNumber));
        return userInfo;
    }

    public String saveUserInfoInString(Jedis jedis, UserInfo userInfo) {
        String temp = info + "saveUserInfoInString" + userInfo.getUserId();
        //return jedis.set(temp, new String(SerializeUtil.serialize(userInfo)));
        return jedis.set(temp, JsonUtil.ObjectToJson(userInfo));
    }

    public UserInfo getUserInfoFromString(Jedis jedis, int id, Class clazz) {
        String temp = info + "saveUserInfoInString" + id;
        //return (UserInfo) SerializeUtil.unserialize(jedis.get(temp).getBytes());
        return (UserInfo) JsonUtil.JsonToObject(jedis.get(temp), clazz);
    }



    public static void main(String[] args) {
        Jedis jedis = RedisConnectionFactory.getConnection();
        UserRedisService service = new UserRedisService();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(1);
        userInfo.setUserName("罗振");
        userInfo.setUserAge(22);
        userInfo.setUserPhoneNumber("18373161908");

        long totalTime1 = 0L;
        long totalTime2 = 0L;
        long totalTime3 = 0L;
        long totalTime4 = 0L;

        for (int i = 0; i < 10; i++) {
            long startTime = System.currentTimeMillis();
            service.saveUserInfoInHash(jedis, userInfo);
            long endTime = System.currentTimeMillis();
            totalTime1 += endTime - startTime;
            System.out.println("save userInfo in hash spend :" + (endTime - startTime) + "ms");

            startTime = System.currentTimeMillis();
            System.out.println(service.getUserInfoFromHash(jedis, 1));
            endTime = System.currentTimeMillis();
            totalTime2 += endTime - startTime;
            System.out.println("get userInfo from hash spend : " + (endTime - startTime) + "ms");

            startTime = System.currentTimeMillis();
            service.saveUserInfoInString(jedis, userInfo);
            endTime = System.currentTimeMillis();
            totalTime3 += endTime - startTime;
            System.out.println("save userInfo in string spend : " + (endTime - startTime) + "ms");

            startTime = System.currentTimeMillis();
            System.out.println(service.getUserInfoFromString(jedis, 1, UserInfo.class));
            endTime = System.currentTimeMillis();
            totalTime4 += endTime - startTime;
            System.out.println("get userInfo from string spend : " + (endTime - startTime) + "ms");

            jedis.flushAll();
        }
        System.out.println("-------------------------------------------------------");
        System.out.println("save userInfo in hash total spend :" + totalTime1 + "ms");
        System.out.println("get userInfo from hash total spend : " + totalTime2 + "ms");
        System.out.println("save userInfo in string total spend : " + totalTime3 + "ms");
        System.out.println("get userInfo from string total spend : " + totalTime4 + "ms");
    }

}
