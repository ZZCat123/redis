package datastructure.list;

import redis.clients.jedis.Jedis;
import utils.JsonUtil;
import utils.RedisConnectionFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Class MessageQueue
 * @Description: TODO
 * @Author: luozhen
 * @Create: 2018/09/27 23:00
 */
public class MessageQueue<T> {

    private final int size;
    private String key;
    private final int timeout;

    private final Jedis jedis = RedisConnectionFactory.getConnection();

    public MessageQueue(int size, String key, int timeout) {
        this.size = size;
        this.key = key;
        this.timeout = timeout;
    }

    public long enqueue(String string) {
//        if (jedis.llen(this.key) >= size) {
//            throw new Exception("The queue is full");
//        } else {
            return jedis.lpush(this.key, string);
//        }
    }

    public List<String> dequeue(String key) {
        return jedis.brpop(timeout ,key);
    }

    public static void main(String[] args) throws InterruptedException {

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        final int total = 10;
        MessageQueue messageQueue = new MessageQueue(5, "messageQueue", 15);
        ExecutorService service = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(total);
        service.execute(() -> {
            for (int i = 0; i < total; i++) {
                try {
                    messageQueue.enqueue("message");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        for (int i = 0; i < total; i++) {
            service.execute(() -> {
                System.out.println(messageQueue.dequeue("messageQueue").toString());
                latch.countDown();
            });
        }
        latch.await();
        service.shutdown();
    }

}
