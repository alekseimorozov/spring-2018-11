package ru.otus.training.alekseimorozov.quiz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.training.alekseimorozov.quiz.dao.QuestionDao;
import ru.otus.training.alekseimorozov.quiz.entity.Question;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {
    private QuestionDao questionDao;

    @Autowired
    public QuizServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public List<Question> getQuizQuestions(String fileName) {
        return questionDao.findAll(fileName);
    }
}