package com.qming.question2answer.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-27
 * Time: 13:58
 */
@Service
public class JedisAdaptor implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(JedisAdaptor.class);

    private JedisPool pool;

    public static void main(String[] args) throws Exception {
        JedisAdaptor jedisAdaptor = new JedisAdaptor();
        jedisAdaptor.afterPropertiesSet();

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://127.0.0.1:6379/10");
    }

    public Jedis getJedis() {

        return pool.getResource();
    }

    public void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public long scrad(String key) {

        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.scard(key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("redis处理错误", e.getMessage());
        } finally {
            closeJedis(jedis);
        }
        return 0;
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("redis处理错误", e.getMessage());
        } finally {
            closeJedis(jedis);
        }
        return 0;
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("redis处理错误", e.getMessage());
        } finally {
            closeJedis(jedis);
        }
        return 0;
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("redis处理错误", e.getMessage());
        } finally {
            closeJedis(jedis);
        }
        return false;
    }

    public long lputh(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("redis处理错误", e.getMessage());
        } finally {
            closeJedis(jedis);
        }
        return 0;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            //0表示无元素弹出时一直阻塞
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("redis处理错误", e.getMessage());
        } finally {
            closeJedis(jedis);
        }
        return null;
    }

    public long zadd(String key, double score, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zadd(key, score, value);
        } catch (Exception e) {
            logger.error("redis处理错误", e.getMessage());
        } finally {
            closeJedis(jedis);
        }
        return 0;
    }

    public long zrem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zrem(key, value);
        } catch (Exception e) {
            logger.error("redis处理错误", e.getMessage());
        } finally {
            closeJedis(jedis);
        }
        return 0;
    }

    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("redis处理错误", e.getMessage());
        } finally {
            closeJedis(jedis);
        }
        return 0;
    }

    public Double zscore(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zscore(key, value);
        } catch (Exception e) {
            logger.error("redis处理错误", e.getMessage());
        } finally {
            closeJedis(jedis);
        }
        return null;
    }

    public Set<String> zrevrane(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("redis处理错误", e.getMessage());
        } finally {
            closeJedis(jedis);
        }
        return null;
    }

    public Set<String> zrane(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            logger.error("redis处理错误", e.getMessage());
        } finally {
            closeJedis(jedis);
        }
        return null;
    }

    public Transaction multi(Jedis jedis) {

        try {
            return jedis.multi();
        } catch (Exception e) {
            logger.error("redis处理错误", e.getMessage());
        } finally {
        }
        return null;
    }

    public List<Object> exe(Transaction tx, Jedis jedis) {
        try {
            return tx.exec();
        } catch (Exception e) {
            logger.error("redis处理错误", e.getMessage());
            tx.discard();
        } finally {
            if (tx != null) {
                try {
                    tx.close();
                } catch (IOException e) {
                    logger.error("redis处理错误", e.getMessage());
                }
            }
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

}
