package com.qming.question2answer.service;

import com.qming.question2answer.dao.TicketDao;
import com.qming.question2answer.dao.UserDao;
import com.qming.question2answer.model.HostHolder;
import com.qming.question2answer.model.LoginTicket;
import com.qming.question2answer.model.User;
import com.qming.question2answer.util.SHAUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-23
 * Time: 14:30
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserDao userDao;
    @Autowired
    TicketDao ticketDao;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    SensitiveService sensitiveService;

    public User getUserById(int userId) {
        return userDao.selectUserById(userId);
    }

    public Map<String, String> login(String username, String password) throws Exception {

        Map<String, String> map = new HashMap<>();

        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userDao.selectUserByName(username);
        if (user == null || !SHAUtil.encryptSHA(password + user.getSalt()).equals(user.getPassword())) {

            map.put("msg", "用户名或密码不正确");
            return map;
        }
        String ticket = addTicket(user.getId());
        map.put("ticket", ticket);

        return map;
    }

    public Map<String, String> reg(String username, String password) throws Exception {

        Map<String, String> map = new HashMap<>();

        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        if (userDao.selectUserByName(username) != null) {
            map.put("msg", "用户名已存在");
            return map;
        }
        if (sensitiveService.isSensitive(username)) {
            map.put("msg", "用户名包含敏感词汇");
            return map;
        }

        User user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(SHAUtil.encryptSHA(password + user.getSalt()));
        userDao.insertUser(user);

        String ticket = addTicket(user.getId());
        map.put("ticket", ticket);

        return map;
    }

    public void logout(String ticket) {
        if (hostHolder.get() != null) {
            hostHolder.clear();
        }
        ticketDao.updateStatus(ticket, 1);
    }

    private String addTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setUserId(userId);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));

        ticketDao.insertTicket(ticket);
        return ticket.getTicket();
    }
}
