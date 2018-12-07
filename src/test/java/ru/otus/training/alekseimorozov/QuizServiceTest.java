package ru.otus.training.alekseimorozov;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import ru.otus.training.alekseimorozov.quize.dao.QuestionDao;
import ru.otus.training.alekseimorozov.quize.dao.QuestionDaoImpl;
import ru.otus.training.alekseimorozov.quize.service.QuizService;
import ru.otus.training.alekseimorozov.quize.service.QuizServiceImpl;

import java.io.IOException;

public class QuizServiceTest {
    @Test
    public void getQuizQuestionsTest() throws IOException {
        QuestionDao questionDao = mock(QuestionDaoImpl.class);
        QuizService quizService = new QuizServiceImpl(questionDao);
        quizService.getQuizQuestions();
        verify(questionDao).findAll();
    }
}