package com.qming.question2answer.async;

import com.alibaba.fastjson.JSON;
import com.qming.question2answer.util.JedisAdaptor;
import com.qming.question2answer.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;


/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-27
 * Time: 22:32
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    private Map<EventType, List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();
    private ApplicationContext applicationContext;

    @Autowired
    private JedisAdaptor jedisAdaptor;

    @Override
    public void afterPropertiesSet() throws Exception {

        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType eventType : eventTypes) {
                    if (!config.containsKey(eventType)) {
                        config.put(eventType, new ArrayList<EventHandler>());
                    }
                    config.get(eventType).add(entry.getValue());
                }
            }
        }
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(20);
        ExecutorService threadService = new ThreadPoolExecutor(3, 5, 50, TimeUnit.MILLISECONDS, blockingQueue);
        threadService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    String key = RedisKeyUtil.getEventQueue();
                    List<String> event = jedisAdaptor.brpop(0, key);
                    for (String message : event) {

                        if (Objects.equals(message, key)) {
                            continue;
                        }
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        if (!config.containsKey(eventModel.getEventType())) {
                            logger.error("不支持事件类型" + eventModel.getEventType());
                            continue;
                        }
                        for (EventHandler handler : config.get(eventModel.getEventType())) {
                            handler.doHandler(eventModel);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
