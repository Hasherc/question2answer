package com.qming.question2answer.controller;

import com.qming.question2answer.async.EventModel;
import com.qming.question2answer.async.EventProducer;
import com.qming.question2answer.async.EventType;
import com.qming.question2answer.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-24
 * Time: 17:59
 */
@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = "/reg/", method = RequestMethod.POST)
    public String reg(@RequestParam(name = "username") String username,
                      @RequestParam(name = "password") String password,
                      @RequestParam(name = "rememberme", defaultValue = "false") boolean remember,
                      @RequestParam(name = "next", required = false) String next,
                      HttpServletResponse response,
                      Model model) {
        try {
            Map<String, String> results = userService.reg(username, password);
            if (results.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", results.get("ticket"));
                cookie.setPath("/");
                if (remember) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                response.addCookie(cookie);
                if (!StringUtils.isBlank(next)) {
                    return "redirect:" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", results.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            model.addAttribute("msg", "服务器错误");
            return "login";
        }

    }

    @RequestMapping(path = "/login/", method = RequestMethod.POST)
    public String login(@RequestParam(name = "username") String username,
                        @RequestParam(name = "password") String password,
                        @RequestParam(name = "remember_me", defaultValue = "false") boolean remember,
                        @RequestParam(name = "next", required = false) String next,
                        HttpServletResponse response,
                        Model model) {

        try {
            Map<String, String> results = userService.login(username, password);
            if (results.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", results.get("ticket"));
                cookie.setPath("/");
                if (remember) {
                    cookie.setMaxAge(3600 * 24 * 5);
                }
                EventModel eventModel = new EventModel();
                eventModel.setEventType(EventType.MAIL)
                        .setExt("username", username)
                        .setExt("mail", "qming_c@qq.com");
                eventProducer.fireEvent(eventModel);
                response.addCookie(cookie);
                if (!StringUtils.isBlank(next)) {
                    return "redirect:" + next;
                }
                return "redirect:/";
            } else {
                model.addAttribute("msg", results.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            logger.error("登录异常" + e.getMessage());
            model.addAttribute("msg", "服务器错误");
            return "login";
        }

    }

    @RequestMapping(path = "/reglogin", method = RequestMethod.GET)
    public String regLogin(@RequestParam(name = "next", required = false) String next, Model model) {
        model.addAttribute("next", next);
        return "login";
    }

    @RequestMapping(path = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

}
