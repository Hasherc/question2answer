package com.qming.question2answer.controller;

import com.qming.question2answer.async.EventModel;
import com.qming.question2answer.async.EventProducer;
import com.qming.question2answer.async.EventType;
import com.qming.question2answer.model.Comment;
import com.qming.question2answer.model.HostHolder;
import com.qming.question2answer.service.CommentService;
import com.qming.question2answer.service.LikeService;
import com.qming.question2answer.util.JsonUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-27
 * Time: 15:32
 */
@Controller
public class LikeController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    LikeService likeService;
    @Autowired
    CommentService commentService;
    @Autowired
    private EventProducer eventProducer;

    @ResponseBody
    @RequestMapping(path = "/like", method = RequestMethod.POST)
    public String like(@Param("commentId") int commentId) {
        if (hostHolder.get() == null) {
            return JsonUtil.getJSONString(999);
        }

        int likeCount = likeService.like(hostHolder.get().getId(), JsonUtil.ENTITY_TYPE_COMMENT, commentId);
        //异步处理队列
        Comment comment = commentService.getCommentByCommentId(commentId);

        EventModel model = new EventModel(EventType.LIKE);
        model.setActorId(hostHolder.get().getId())
                .setEntityId(commentId)
                .setEntityOwnerId(comment.getUserId())
                .setExt("questionId", String.valueOf(comment.getEntityId()))
                .setEntityType(JsonUtil.ENTITY_TYPE_COMMENT);

        eventProducer.fireEvent(model);
        return JsonUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @ResponseBody
    @RequestMapping(path = "/dislike", method = RequestMethod.POST)
    public String diLike(@Param("commentId") int commentId) {
        if (hostHolder.get() == null) {
            return JsonUtil.getJSONString(999);
        }
        int likeCount = likeService.dislike(hostHolder.get().getId(), JsonUtil.ENTITY_TYPE_COMMENT, commentId);
        return JsonUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
