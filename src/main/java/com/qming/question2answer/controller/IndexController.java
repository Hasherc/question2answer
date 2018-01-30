package com.qming.question2answer.controller;

import com.qming.question2answer.model.HostHolder;
import com.qming.question2answer.model.Question;
import com.qming.question2answer.model.User;
import com.qming.question2answer.model.ViewObject;
import com.qming.question2answer.service.*;
import com.qming.question2answer.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-23
 * Time: 14:23
 */
@Controller
public class IndexController {
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    LikeService likeService;
    @Autowired
    FollowService followService;
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        model.addAttribute("vos", getQuestions(0, 0, 10));
        return "index";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(@PathVariable int userId, Model model) {

        model.addAttribute("vos", getQuestions(userId, 0, 10));
        User user = userService.getUserById(userId);
        if (user == null) {
            user = userService.getUserById(0);
        }
        model.addAttribute("profileUser", user);
        model.addAttribute("followerCount", followService.getFollowerCount(JsonUtil.ENTITY_TYPE_USER, userId));
        model.addAttribute("followeeCount", followService.getFolloweeCount(JsonUtil.ENTITY_TYPE_USER, userId));
        model.addAttribute("commentCount", commentService.getUserCommentCount(userId));
        if (hostHolder.get() == null) {
            model.addAttribute("followed", false);
        } else {
            model.addAttribute("followed", followService.isFollower(hostHolder.get().getId(), JsonUtil.ENTITY_TYPE_USER, userId));
        }
        return "profile";
    }

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questions = questionService.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questions) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            User user = userService.getUserById(question.getUserId());
            if (user == null) {
                user = userService.getUserById(0);
            }
            vo.set("user", userService.getUserById(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
}
