package com.qming.question2answer;

import com.qming.question2answer.dao.QuestionDao;
import com.qming.question2answer.dao.UserDao;
import com.qming.question2answer.util.MailSenderUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@MapperScan("com.qming.question2answer.dao")
@SpringBootTest
public class Question2answerApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(Question2answerApplicationTests.class);
    @Autowired
    QuestionDao questionDao;
    @Autowired
    UserDao userDao;
    @Autowired
    MailSenderUtil mailSenderUtil;

    @Test
    public void contextLoads() throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("username", "qming_c");
        mailSenderUtil.sendWithHtmlTemplate("915046763@qq.com", "test", "/mail/loginException", map);

    }


}
