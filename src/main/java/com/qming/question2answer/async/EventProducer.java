package com.qming.question2answer.async;

import com.alibaba.fastjson.JSONObject;
import com.qming.question2answer.util.JedisAdaptor;
import com.qming.question2answer.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-27
 * Time: 22:28
 */
@Service
public class EventProducer {
    @Autowired
    JedisAdaptor jedisAdaptor;

    public boolean fireEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueue();
            jedisAdaptor.lputh(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
