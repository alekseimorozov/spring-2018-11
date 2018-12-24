package ru.otus.training.alekseimorozov.quiz.service;

import ru.otus.training.alekseimorozov.quiz.entity.Question;
import java.util.List;

public interface QuizService {
    List<Question> getQuizQuestions(String fileName);
}