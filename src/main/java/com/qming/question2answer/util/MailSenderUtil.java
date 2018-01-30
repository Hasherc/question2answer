package com.qming.question2answer.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-28
 * Time: 15:11
 */
@Service
public class MailSenderUtil implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSenderUtil.class);

    @Autowired
    private Configuration configuration;

    private JavaMailSenderImpl mailSender;


    public boolean sendWithHtmlTemplate(String to,
                                        String subject,
                                        String template,
                                        Map<String, Object> model) {
        try {
            String name = MimeUtility.encodeText("qming_c");
            InternetAddress from = new InternetAddress(name + "<qming_c@foxmail.com>");


            Template t = configuration.getTemplate(template + ".ftl");
            configuration.setDefaultEncoding("UTF-8");
            String result = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result, true);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        //configuration = new Configuration(Configuration.VERSION_2_3_27);
        configuration.setTemplateLoader(new SpringTemplateLoader(new DefaultResourceLoader(), "classpath:/templates"));

        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("qming_c@foxmail.com");
        mailSender.setPassword("luvlmgwoikzhbfhg");
        mailSender.setHost("smtp.qq.com");
        mailSender.setPort(465);
        mailSender.setProtocol("smtp");
        mailSender.setDefaultEncoding("utf8");

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);


    }


}
