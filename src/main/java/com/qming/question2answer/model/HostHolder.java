package com.qming.question2answer.model;

import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-24
 * Time: 20:33
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public void set(User user) {
        users.set(user);
    }

    public User get() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
