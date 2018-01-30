package com.qming.question2answer.service;

import com.qming.question2answer.util.JedisAdaptor;
import com.qming.question2answer.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-27
 * Time: 14:44
 */
@Service
public class LikeService {
    private static final Logger logger = LoggerFactory.getLogger(LikeService.class);

    @Autowired
    JedisAdaptor jedisAdaptor;

    public int getLikeCount(int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return (int) jedisAdaptor.scrad(likeKey);
    }

    public int like(int userId, int entityType, int entityId) {

        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdaptor.sadd(likeKey, String.valueOf(userId));

        String dislikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdaptor.srem(dislikeKey, String.valueOf(userId));
        return (int) jedisAdaptor.scrad(likeKey);
    }

    public int dislike(int userId, int entityType, int entityId) {

        String dislikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdaptor.sadd(dislikeKey, String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdaptor.srem(likeKey, String.valueOf(userId));

        return (int) jedisAdaptor.scrad(likeKey);
    }

    public boolean isLikeMember(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return jedisAdaptor.sismember(likeKey, String.valueOf(userId));
    }

    public boolean isDislikeMember(int userId, int entityType, int entityId) {
        String dislikeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return jedisAdaptor.sismember(dislikeKey, String.valueOf(userId));
    }

    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdaptor.sismember(likeKey, String.valueOf(userId))) {
            return 1;
        }

        String dislikeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdaptor.sismember(likeKey, String.valueOf(userId))) {
            return -1;
        }
        return 0;
    }
}
