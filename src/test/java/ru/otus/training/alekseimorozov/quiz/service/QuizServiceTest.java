package ru.otus.training.alekseimorozov.quiz.service;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.training.alekseimorozov.quiz.dao.QuestionDao;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false",
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"
})
public class QuizServiceTest {
    @MockBean
    private QuestionDao questionDao;

    @Autowired
    private QuizService quizService;

    @Test
    public void getQuizQuestionsTest() throws IOException {
        quizService.getQuizQuestions("questions.csv");
        verify(questionDao).findAll("questions.csv");
    }
}