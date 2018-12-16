package ru.otus.training.alekseimorozov;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.training.alekseimorozov.quize.controller.QuizController;
import ru.otus.training.alekseimorozov.quize.dao.QuestionDao;
import ru.otus.training.alekseimorozov.quize.dao.QuestionDaoImpl;
import ru.otus.training.alekseimorozov.quize.service.QuizService;
import ru.otus.training.alekseimorozov.quize.service.QuizServiceImpl;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan
public class Config {
    @Bean
    public QuestionDao mockQuestionDao() {
        return mock(QuestionDaoImpl.class);
    }

    @Bean
    public QuizService testQuizService() {
        return new QuizServiceImpl(mockQuestionDao());
    }

    @Bean
    public QuizController testQuizController() {
        return new QuizController(mockQuizService(), mockMessageSource());
    }

    @Bean QuizService mockQuizService() {
        return mock(QuizService.class);
    }

    @Bean
    public MessageSource mockMessageSource() {
        return mock(MessageSource.class);
    }
}