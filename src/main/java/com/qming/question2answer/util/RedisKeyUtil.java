package com.qming.question2answer.util;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-27
 * Time: 14:47
 */
public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String BIZ_LIKE = "LIKE";
    private static final String BIZ_DISLIKE = "DISLIKE";
    private static final String BIZ_EVENT_QUEUE = "EVENT_QUEUE";
    private static final String BIZ_FOLLOWER = "FOLLOWER";
    private static final String BIZ_FOLLOWEE = "FOLLOWEE";

    public static String getLikeKey(int entityType, int entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getEventQueue() {
        return BIZ_EVENT_QUEUE;
    }

    public static String getFollowerKey(int entityType, int entityId) {
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getFolloweeKey(int entityType, int entityId) {
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

}
