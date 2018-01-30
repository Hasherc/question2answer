package com.qming.question2answer.controller;

import com.qming.question2answer.async.EventModel;
import com.qming.question2answer.async.EventProducer;
import com.qming.question2answer.async.EventType;
import com.qming.question2answer.model.HostHolder;
import com.qming.question2answer.model.Question;
import com.qming.question2answer.model.User;
import com.qming.question2answer.model.ViewObject;
import com.qming.question2answer.service.CommentService;
import com.qming.question2answer.service.FollowService;
import com.qming.question2answer.service.QuestionService;
import com.qming.question2answer.service.UserService;
import com.qming.question2answer.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-29
 * Time: 17:33
 *
 * @author qming_c
 */
@Controller
public class FollowController {
    @Autowired
    EventProducer eventProducer;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private FollowService followService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    /**
     * 关注用户
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/followUser", method = RequestMethod.POST)
    public String followUser(@RequestParam("userId") int userId) {
        if (hostHolder.get() == null) {
            return JsonUtil.getJSONString(999);
        }
        boolean result = followService.follow(hostHolder.get().getId(), JsonUtil.ENTITY_TYPE_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.get().getId())
                .setEntityOwnerId(userId)
                .setEntityType(JsonUtil.ENTITY_TYPE_USER)
        );

        return JsonUtil.getJSONString(result ? 0 : 1, String.valueOf(followService.getFolloweeCount(JsonUtil.ENTITY_TYPE_USER, hostHolder.get().getId())));
    }

    /**
     * 取关用户
     *
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(path = "/unfollowUser", method = RequestMethod.POST)
    public String unfollowUser(@RequestParam("userId") int userId) {
        if (hostHolder.get() == null) {
            return JsonUtil.getJSONString(999);
        }
        boolean result = followService.unfollow(hostHolder.get().getId(), JsonUtil.ENTITY_TYPE_USER, userId);

        return JsonUtil.getJSONString(result ? 0 : 1, String.valueOf(followService.getFolloweeCount(JsonUtil.ENTITY_TYPE_USER, hostHolder.get().getId())));
    }

    @ResponseBody
    @RequestMapping(path = "/followQuestion", method = RequestMethod.POST)
    public String followQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.get() == null) {
            return JsonUtil.getJSONString(999);
        }
        Question question = questionService.getQuestionById(questionId);
        if (question == null) {
            return JsonUtil.getJSONString(1, "问题不存在");
        }
        boolean result = followService.follow(hostHolder.get().getId(), JsonUtil.ENTITY_TYPE_QUESTION, questionId);
        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.get().getHeadUrl());
        info.put("name", hostHolder.get().getName());
        info.put("id", hostHolder.get().getId());
        info.put("count", followService.getFollowerCount(JsonUtil.ENTITY_TYPE_QUESTION, questionId));
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                .setActorId(hostHolder.get().getId())
                .setEntityOwnerId(question.getUserId())
                .setEntityId(questionId)
                .setEntityType(JsonUtil.ENTITY_TYPE_QUESTION));
        return JsonUtil.getJSONString(result ? 0 : 1, info);
    }

    @ResponseBody
    @RequestMapping(path = "/unfollowQuestion", method = RequestMethod.POST)
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {
        if (hostHolder.get() == null) {
            return JsonUtil.getJSONString(999);
        }
        Question question = questionService.getQuestionById(questionId);
        if (question == null) {
            return JsonUtil.getJSONString(1, "问题不存在");
        }
        boolean result = followService.unfollow(hostHolder.get().getId(), JsonUtil.ENTITY_TYPE_QUESTION, questionId);
        Map<String, Object> info = new HashMap<>();
        info.put("id", hostHolder.get().getId());
        info.put("count", followService.getFollowerCount(JsonUtil.ENTITY_TYPE_QUESTION, questionId));

        return JsonUtil.getJSONString(result ? 0 : 1, info);
    }

    @RequestMapping(path = "/user/{userId}/followers", method = RequestMethod.GET)
    public String followers(@PathVariable("userId") int userId, Model model) {
        List<Integer> followerIds = followService.getFollowers(JsonUtil.ENTITY_TYPE_USER, userId, 10);
        if (hostHolder.get() != null) {
            model.addAttribute("followers", getUsersInfo(hostHolder.get().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        User curUser = userService.getUserById(userId);
        if (curUser == null) {
            return "redirect:/user/0/followers";
        }
        model.addAttribute("followerCount", followService.getFollowerCount(JsonUtil.ENTITY_TYPE_USER, userId));
        model.addAttribute("curUser", userService.getUserById(userId));
        return "followers";
    }

    @RequestMapping(path = "/user/{userId}/followees", method = RequestMethod.GET)
    public String followees(@PathVariable("userId") int userId, Model model) {
        List<Integer> followeeIds = followService.getFollowees(JsonUtil.ENTITY_TYPE_USER, userId, 10);
        if (hostHolder.get() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.get().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(JsonUtil.ENTITY_TYPE_USER, userId));
        User curUser = userService.getUserById(userId);
        if (curUser == null) {
            return "redirect:/user/0/followees";
        }
        model.addAttribute("curUser", curUser);
        return "followees";
    }

    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> vos = new ArrayList<>();
        for (int userId : userIds) {
            User user = userService.getUserById(userId);
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("commentCount", commentService.getUserCommentCount(userId));
            vo.set("followerCount", followService.getFollowerCount(JsonUtil.ENTITY_TYPE_USER, userId));
            vo.set("followeeCount", followService.getFolloweeCount(JsonUtil.ENTITY_TYPE_USER, userId));
            vo.set("followed", followService.isFollower(localUserId, JsonUtil.ENTITY_TYPE_USER, userId));

//            if (localUserId != 0){
//            }else {
//                vo.set("followed", false);
//            }
            vos.add(vo);
        }
        return vos;
    }

}
