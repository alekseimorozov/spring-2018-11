package ru.otus.training.alekseimorozov.quiz.dao;

import ru.otus.training.alekseimorozov.quiz.entity.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll(String sourceName);
}