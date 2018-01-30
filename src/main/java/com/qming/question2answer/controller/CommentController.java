package com.qming.question2answer.controller;

import com.qming.question2answer.dao.QuestionDao;
import com.qming.question2answer.model.Comment;
import com.qming.question2answer.model.HostHolder;
import com.qming.question2answer.service.CommentService;
import com.qming.question2answer.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-26
 * Time: 13:00
 */
@Controller
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    QuestionDao questionDao;

    @RequestMapping(path = {"/addComment"}, method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            if (hostHolder.get() == null) {
                comment.setUserId(JsonUtil.ANONYMOUS_USER_ID);
            } else {
                comment.setUserId(hostHolder.get().getId());
            }
            comment.setCreatedDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(JsonUtil.ENTITY_TYPE_QUESTION);
            comment.setStatus(0);
            comment.setContent(content);
            commentService.addComment(comment);
            int count = commentService.getCommentCount(comment.getEntityId(), JsonUtil.ENTITY_TYPE_QUESTION);
            questionDao.updateStatus(questionId, count);
        } catch (Exception e) {
            logger.error("增加评论失败" + e.getMessage());
        }

        return "redirect:/question/" + questionId;
    }
}
