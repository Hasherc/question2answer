package com.qming.question2answer.service;

import com.qming.question2answer.dao.CommentDao;
import com.qming.question2answer.model.Comment;
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
 * Time: 12:47
 */
@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);
    @Autowired
    CommentDao commentDao;


    @Autowired
    SensitiveService sensitiveService;

    public List<Comment> getCommentByEntity(int entityId, int entityType) {
        return commentDao.selectCommentsByEntity(entityId, entityType);
    }

    public Comment getCommentByCommentId(int commentId) {
        return commentDao.selectCommentsById(commentId);
    }

    public int addComment(Comment comment) {
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.sensitiveFilter(comment.getContent()));
        return commentDao.insertComment(comment) > 0 ? comment.getId() : 0;
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDao.getCommentCount(entityId, entityType);
    }

    public int getUserCommentCount(int userId) {
        return commentDao.getUserCommentCount(userId);
    }

    public boolean deleteComment(int commentId) {
        return commentDao.updateStatus(commentId, 1) > 0;
    }

}
