package com.qming.question2answer.controller;

import com.qming.question2answer.model.*;
import com.qming.question2answer.service.*;
import com.qming.question2answer.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-25
 * Time: 19:02
 */
@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    SensitiveService sensitiveService;
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    LikeService likeService;
    @Autowired
    FollowService followService;

    @RequestMapping(path = "/question/add", method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {
        try {
            Question question = new Question();
            title = HtmlUtils.htmlEscape(title);
            content = HtmlUtils.htmlEscape(content);
            question.setTitle(sensitiveService.sensitiveFilter(title));
            question.setContent(sensitiveService.sensitiveFilter(content));
            question.setCreatedDate(new Date());
            if (hostHolder.get() != null) {
                question.setUserId(hostHolder.get().getId());
            } else {
                question.setUserId(JsonUtil.ANONYMOUS_USER_ID);
            }
            if (questionService.addQuestion(question) > 0) {
                return JsonUtil.getJSONString(0);
            }
        } catch (Exception e) {
            logger.error("增加题目失败" + e.getMessage());
        }

        return JsonUtil.getJSONString(1, "服务器错误");
    }

    @RequestMapping(path = "/question/{qid}", method = RequestMethod.GET)
    public String questionDetail(@PathVariable("qid") int qId,
                                 Model model) {

        try {
            List<Comment> commentList = commentService.getCommentByEntity(qId, JsonUtil.ENTITY_TYPE_QUESTION);
            List<ViewObject> comments = new ArrayList<>();
            for (Comment comment : commentList) {
                ViewObject viewObject = new ViewObject();
                viewObject.set("user", userService.getUserById(comment.getUserId()));
                viewObject.set("comment", comment);
                viewObject.set("likeCount", likeService.getLikeCount(JsonUtil.ENTITY_TYPE_COMMENT, comment.getId()));
                if (hostHolder.get() != null) {
                    viewObject.set("liked", likeService.getLikeStatus(hostHolder.get().getId(), JsonUtil.ENTITY_TYPE_COMMENT, comment.getId()));
                } else {
                    viewObject.set("liked", 0);
                }
                comments.add(viewObject);
            }
            model.addAttribute("comments", comments);
            List<ViewObject> followUsers = new ArrayList<>();
            List<Integer> userIds = followService.getFollowers(JsonUtil.ENTITY_TYPE_QUESTION, qId, 20);
            for (int userId : userIds) {
                ViewObject vo = new ViewObject();
                User user = userService.getUserById(userId);
                if (user == null) {
                    continue;
                }
                vo.set("name", user.getName());
                vo.set("id", user.getId());
                vo.set("headUrl", user.getHeadUrl());
                followUsers.add(vo);
            }
            model.addAttribute("followUsers", followUsers);
            if (hostHolder.get() != null) {
                model.addAttribute("followed", followService.isFollower(hostHolder.get().getId(), JsonUtil.ENTITY_TYPE_QUESTION, qId));
            } else {
                model.addAttribute("followed", false);
            }

            model.addAttribute("question", questionService.getQuestionById(qId));

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取问题失败" + e.getMessage());

        }
        return "detail";
    }
}
