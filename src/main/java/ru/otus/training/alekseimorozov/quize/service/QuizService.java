package ru.otus.training.alekseimorozov.quize.service;

import ru.otus.training.alekseimorozov.quize.entity.Question;
import java.util.List;

public interface QuizService {
    List<Question> getQuizQuestions(String fileName);
}