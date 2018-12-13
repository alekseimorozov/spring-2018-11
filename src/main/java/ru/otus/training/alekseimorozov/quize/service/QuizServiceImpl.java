package ru.otus.training.alekseimorozov.quize.service;

import ru.otus.training.alekseimorozov.quize.dao.QuestionDao;
import ru.otus.training.alekseimorozov.quize.entity.Question;

import java.util.List;

public class QuizServiceImpl implements QuizService {
    private QuestionDao questionDao;

    public QuizServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public List<Question> getQuizQuestions(String fileName) {
        return questionDao.findAll(fileName);
    }

}
