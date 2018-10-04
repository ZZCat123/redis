package demo;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import redis.clients.jedis.Jedis;
import utils.MySQLUtil;
import utils.RedisConnectionFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Class CompareRedisAndMySQL
 * @Description: MySQL与Redis性能对比
 *               MySQL execute 1000 times update spend 4294ms
 *               Redis do the same operations spend 331ms
 *               mysql spend 12 times than redis
 *               ---------------------------------------
 *               MySQL execute 10000 times update spend 30379ms
 *               Redis do the same operations spend 614ms
 *               mysql spend 49 times than redis
 *               ---------------------------------------
 *               MySQL execute 100000 times update spend 253495ms
 *               Redis do the same operations spend 4743ms
 *               mysql spend 53 times than redis
 *               ---------------------------------------
 * @Author: luozhen
 * @Create: 2018/10/02 20:37
 */
public class CompareRedisAndMySQL {

    private static void testMySQL(int threshold) {
        Connection connection = MySQLUtil.getConnection();
        String sql = "update user set user_phone = user_phone + 1 where user_id = 1";
        try {
            PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
            for (int i = 0; i < threshold; i++) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MySQLUtil.closeConnection(connection);
        }
    }

    private static void testRedis(int threshold) {
        Connection connection = MySQLUtil.getConnection();
        Jedis jedis = RedisConnectionFactory.getConnection();
        String sql = "select * from user where user_id = 1";
        try {
            PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            String username = "";
            int userAge = 0;
            while (resultSet.next()) {
                username = resultSet.getString("user_name");
                userAge = resultSet.getInt("user_age");
            }
            jedis.set(username, String.valueOf(userAge));
            for (int i = 0; i < threshold; i++) {
                jedis.incr(username);
            }
            userAge = Integer.parseInt(jedis.get(username));
            jedis.flushAll();
            sql = "update user set user_age = ? where user_id = 1";
            preparedStatement = (PreparedStatement) connection.prepareStatement(sql);
            preparedStatement.setInt(1, userAge);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            RedisConnectionFactory.closeConnection();
            MySQLUtil.closeConnection(connection);
        }
    }

    public static void main(String[] args) {
//        ExecutorService service = Executors.newCachedThreadPool();
//        CountDownLatch latch = new CountDownLatch(threshold / 100);
        for (int i = 1000; i >= 10; i /= 10) {
            int threshold = 1000000;
            threshold /= i;
            long startTime = System.currentTimeMillis();
//        for (int i = 0; i < threshold / 100; i++) {
//            service.execute(() -> {
//                CompareRedisAndMySQL.testMySQL(100);
//                latch.countDown();
//            });
//        }
//        latch.await();
            CompareRedisAndMySQL.testMySQL(threshold);
            long endTime = System.currentTimeMillis();
            long mysqlSpend = endTime - startTime;
            System.out.println("MySQL execute " + threshold + " times update spend " + mysqlSpend + "ms");
            startTime = System.currentTimeMillis();
//        for (int i = 0; i < 10; i++) {
//            service.execute(() -> CompareRedisAndMySQL.testRedis(threshold));
//        }
            CompareRedisAndMySQL.testRedis(threshold);
            endTime = System.currentTimeMillis();
            long redisSpend = endTime - startTime;
            System.out.println("Redis do the same operations spend " + redisSpend + "ms");
            System.out.println("mysql spend " + mysqlSpend / redisSpend + " times than redis");
            System.out.println("---------------------------------------");
//        service.shutdown();
        }
    }
}
