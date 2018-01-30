package com.qming.question2answer.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-23
 * Time: 14:37
 */
public class ViewObject {
    Map<String, Object> map = new HashMap<>();

    public Object get(String key) {
        return map.get(key);
    }

    public void set(String key, Object object) {
        map.put(key, object);
    }
}
