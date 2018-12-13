package ru.otus.training.alekseimorozov.quize.dao;

import ru.otus.training.alekseimorozov.quize.entity.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll(String sourceName);
}
