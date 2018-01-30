package com.qming.question2answer.service;

import com.qming.question2answer.dao.QuestionDao;
import com.qming.question2answer.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: qming_c
 * Date: 2018-01-23
 * Time: 14:30
 */
@Service
public class QuestionService {
    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);


    @Autowired
    QuestionDao questionDao;

    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDao.selectLatestQuestions(userId, offset, limit);
    }

    public int addQuestion(Question question) {
        return questionDao.addQuestion(question);
    }

    public void updateCommentCount(int id, int commentCount) {
        questionDao.updateStatus(id, commentCount);
    }

    public Question getQuestionById(int id) {
        return questionDao.selectQuestionById(id);
    }
}
