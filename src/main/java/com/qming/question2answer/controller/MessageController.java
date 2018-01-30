package com.qming.question2answer.controller;

import com.qming.question2answer.dao.UserDao;
import com.qming.question2answer.model.HostHolder;
import com.qming.question2answer.model.Message;
import com.qming.question2answer.model.User;
import com.qming.question2answer.model.ViewObject;
import com.qming.question2answer.service.MessageService;
import com.qming.question2answer.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-26
 * Time: 20:10
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    HostHolder hostHolder = new HostHolder();

    @Autowired
    MessageService messageService;

    @Autowired
    UserDao userDao;

    @ResponseBody
    @RequestMapping(path = "/msg/addMessage", method = RequestMethod.POST)
    public String addMessage(@RequestParam("toName") String name,
                             @RequestParam("content") String content) {

        try {
            if (hostHolder.get() == null) {
                return JsonUtil.getJSONString(999, "未登录");
            }
            User target = userDao.selectUserByName(name);
            if (target == null) {
                return JsonUtil.getJSONString(1, "用户不存在");
            }
            Message message = new Message();
            message.setFromId(hostHolder.get().getId());
            message.setToId(target.getId());
            message.setContent(content);
            message.setCreatedDate(new Date());
            messageService.addMessage(message);
            return JsonUtil.getJSONString(0);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("增加站内信失败" + e.getMessage());
            return JsonUtil.getJSONString(1, "插入站内信失败");
        }


    }

    @RequestMapping(path = "/msg/list", method = RequestMethod.GET)
    public String getConversationList(Model model) {
        try {
            User user = hostHolder.get();
            if (user == null) {
                return "redirect:/reglogin";
            }

            List<Message> messages = messageService.getConversationList(hostHolder.get().getId());
            List<ViewObject> vos = new ArrayList<>();

            for (Message message : messages) {
                ViewObject vo = new ViewObject();
                int targetId = message.getFromId() == user.getId() ? message.getToId() : message.getFromId();
                vo.set("user", userDao.selectUserById(targetId));
                vo.set("unReadCount", messageService.getConversationUnReadCount(message.getConversationId()));
                vo.set("message", message);
                vos.add(vo);
            }
            model.addAttribute("vos", vos);

            return "letter";
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
            return "redirect:/";
        }


    }

    @RequestMapping(path = "/msg/{conversationId}", method = RequestMethod.GET)
    public String getMessageList(@PathVariable("conversationId") String conversationId,
                                 Model model) {
        try {
            User user = hostHolder.get();
            if (user == null) {
                return "redirect:/reglogin";
            }
            List<Message> messages = messageService.getMessageList(conversationId);
            List<ViewObject> vos = new ArrayList<>();
            for (Message message : messages) {
                ViewObject vo = new ViewObject();
                int targetId = message.getFromId() == user.getId() ? message.getToId() : message.getFromId();
                vo.set("user", userDao.selectUserById(targetId));
                vo.set("message", message);
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            messageService.markToRead(conversationId);
            return "letterDetail";
        } catch (Exception e) {
            logger.error("获取站内信失败：" + e.getMessage());
            return "redirect:/msg/list";
        }

    }

}
