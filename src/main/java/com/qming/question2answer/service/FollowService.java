package com.qming.question2answer.service;

import com.qming.question2answer.util.JedisAdaptor;
import com.qming.question2answer.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-29
 * Time: 19:00
 */
@Service
public class FollowService {
    @Autowired
    JedisAdaptor jedisAdaptor;

    /**
     * 关注
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean follow(int userId, int entityType, int entityId) {
        //某个用户的粉丝
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        //某个用户所关注的实体
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, userId);
        Date date = new Date();
        Jedis jedis = jedisAdaptor.getJedis();
        Transaction tx = jedisAdaptor.multi(jedis);
        tx.zadd(followerKey, date.getTime(), String.valueOf(userId));
        tx.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
        List<Object> res = jedisAdaptor.exe(tx, jedis);
        return res.size() == 2 && (long) res.get(0) > 0 && (long) res.get(0) > 0;
    }

    /**
     * 取消关注
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean unfollow(int userId, int entityType, int entityId) {
        //某个用户的粉丝
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        //某个用户所关注的实体
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, userId);
        Date date = new Date();
        Jedis jedis = jedisAdaptor.getJedis();
        Transaction tx = jedisAdaptor.multi(jedis);
        tx.zrem(followerKey, String.valueOf(userId));
        tx.zrem(followeeKey, String.valueOf(entityId));
        List<Object> res = jedisAdaptor.exe(tx, jedis);

        return res.size() == 2 && (long) res.get(0) > 0 && (long) res.get(0) > 0;
    }

    /**
     * 获取关注者列表
     *
     * @param entityType 本体类型
     * @param entityId   本体id
     * @param count      获取的关注者数量
     * @return
     */
    public List<Integer> getFollowers(int entityType, int entityId, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdaptor.zrevrane(followerKey, 0, count));
    }

    /**
     * 获取关注着列表
     *
     * @param entityType
     * @param entityId
     * @param offset
     * @param count
     * @return
     */
    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdaptor.zrevrane(followerKey, offset, offset + count));
    }

    /**
     * 获取所关注的实体
     *
     * @param entityType
     * @param userId
     * @param count
     * @return
     */
    public List<Integer> getFollowees(int entityType, int userId, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, userId);
        return getIdsFromSet(jedisAdaptor.zrevrane(followeeKey, 0, count));
    }

    /**
     * 获取所关注的实体
     *
     * @param entityType
     * @param userId
     * @param offset
     * @param count
     * @return
     */
    public List<Integer> getFollowees(int entityType, int userId, int offset, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, userId);
        return getIdsFromSet(jedisAdaptor.zrevrane(followeeKey, offset, offset + count));
    }

    /**
     * 获取被关注数量
     *
     * @param entityType
     * @param entityId
     * @return
     */
    public int getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        System.out.println(followerKey);
        return (int) jedisAdaptor.zcard(followerKey);
    }

    /**
     * 获取某人对某一实体的关注数量
     *
     * @param entityType
     * @param userId
     * @return
     */
    public int getFolloweeCount(int entityType, int userId) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, userId);
        return (int) jedisAdaptor.zcard(followeeKey);
    }

    /**
     * 将redis中返回的set转为list
     *
     * @param idSet
     * @return
     */
    public List<Integer> getIdsFromSet(Set<String> idSet) {
        List<Integer> list = new ArrayList<Integer>();
        for (String str : idSet) {
            list.add(Integer.parseInt(str));
        }
        return list;
    }

    /**
     * 是不是某一实体的关注者
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean isFollower(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        boolean a = jedisAdaptor.zscore(followerKey, String.valueOf(userId)) != null;
        return a;
    }
}
