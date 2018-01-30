package com.qming.question2answer.async.handler;

import com.qming.question2answer.async.EventHandler;
import com.qming.question2answer.async.EventModel;
import com.qming.question2answer.async.EventType;
import com.qming.question2answer.service.UserService;
import com.qming.question2answer.util.MailSenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-28
 * Time: 22:47
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MailSenderUtil mailSenderUtil;
    @Autowired
    UserService userService;

    @Override
    public void doHandler(EventModel model) {
        Map<String, Object> map = new HashMap<>();

        map.put("username", model.getExt("username"));
        mailSenderUtil.sendWithHtmlTemplate(model.getExt("mail"), "test", "mail/loginException", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.MAIL);
    }
}
