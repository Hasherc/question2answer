package com.qming.question2answer.async.handler;

import com.qming.question2answer.async.EventHandler;
import com.qming.question2answer.async.EventModel;
import com.qming.question2answer.async.EventType;
import com.qming.question2answer.model.Message;
import com.qming.question2answer.model.User;
import com.qming.question2answer.service.MessageService;
import com.qming.question2answer.service.UserService;
import com.qming.question2answer.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-28
 * Time: 12:39
 */
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    public void doHandler(EventModel model) {
        Message message = new Message();
        message.setFromId(JsonUtil.SYSTEM_USER_ID);
        message.setCreatedDate(new Date());
        message.setToId(model.getEntityOwnerId());
        User user = userService.getUserById(model.getActorId());
        message.setContent("用户" + user.getName()
                + "赞了您的评论，http://127.0.0.1:8080/question/" + model.getExt("questionId"));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
