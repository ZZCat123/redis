package cluster;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Class JedisClusterFactory
 * @Description: TODO
 * @Author: luozhen
 * @Create: 2018/10/06 16:13
 */
@Data
public class JedisClusterFactory {

    private JedisCluster jedisCluster;

    private List<String> hostPortList;

    // 超时，单位ms
    private int timeout;

    Logger logger = LoggerFactory.getLogger(JedisClusterFactory.class);

    public void init() {
        // 设置相关参数
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        Set<HostAndPort> nodeSet = new HashSet<>();
        for (String hostPort : hostPortList) {
            String[] arr = hostPort.split(":");
            if (arr.length != 2) {
                continue;
            }
            nodeSet.add(new HostAndPort(arr[0], Integer.parseInt(arr[1])));
        }
        try {
            jedisCluster = new JedisCluster(nodeSet, timeout, jedisPoolConfig);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void destory() {
        if (jedisCluster != null) {
            try {
                jedisCluster.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}
