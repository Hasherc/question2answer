package com.qming.question2answer.service;

import com.qming.question2answer.dao.MessageDao;
import com.qming.question2answer.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-26
 * Time: 19:50
 */
@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    MessageDao messageDao;

    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveService.sensitiveFilter(message.getContent()));
        message.setHasRead(0);
        return messageDao.insertMessage(message);
    }

    /**
     * 获取消息目录，并按时间排序
     *
     * @param userId
     * @return
     */
    public List<Message> getConversationList(int userId) {
        return messageDao.selectConversationListByUserId(userId);
    }

    public int getConversationUnReadCount(String conversationId) {
        return messageDao.selectConversationUnreadCount(conversationId);
    }

    public List<Message> getMessageList(String conversationId) {

        return messageDao.selectMessageListByConversationId(conversationId);
    }

    public int markToRead(String conversationId) {
        return messageDao.updateUnRead(conversationId, 1);
    }
}
