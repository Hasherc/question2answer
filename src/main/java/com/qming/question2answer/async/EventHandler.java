package com.qming.question2answer.async;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-27
 * Time: 22:29
 */
@Component
public interface EventHandler {
    void doHandler(EventModel model);

    List<EventType> getSupportEventTypes();
}
