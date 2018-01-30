package com.qming.question2answer.async;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-27
 * Time: 22:27
 */
public enum EventType {
    /**
     * 赞事件
     */
    LIKE(0),
    /**
     * 评论事件
     */
    COMMENT(1),
    /**
     * 登录事件
     */
    lOGIN(2),
    /**
     * 邮件事件
     */
    MAIL(3),
    /**
     * 关注事件
     */
    FOLLOW(4);

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "EventType{" +
                "value=" + value +
                '}';
    }
}
