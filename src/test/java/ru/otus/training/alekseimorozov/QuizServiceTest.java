package ru.otus.training.alekseimorozov;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import ru.otus.training.alekseimorozov.quize.dao.QuestionDao;
import ru.otus.training.alekseimorozov.quize.service.QuizService;

import java.io.IOException;

public class QuizServiceTest extends CommonRules {
    @Test
    public void getQuizQuestionsTest() throws IOException {
        QuestionDao questionDao = (QuestionDao) getContext().getBean("mockQuestionDao");
        QuizService quizService = (QuizService) getContext().getBean("testQuizService");
        quizService.getQuizQuestions("questions.csv");
        verify(questionDao).findAll("questions.csv");
    }
}