package ru.otus.training.alekseimorozov.quize.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import ru.otus.training.alekseimorozov.quize.controller.QuizController;
import ru.otus.training.alekseimorozov.quize.dao.QuestionDao;
import ru.otus.training.alekseimorozov.quize.dao.QuestionDaoImpl;
import ru.otus.training.alekseimorozov.quize.service.QuizService;
import ru.otus.training.alekseimorozov.quize.service.QuizServiceImpl;

@Configuration
public class QuizConfig {
    @Bean
    public QuestionDao questionDao() {
        return new QuestionDaoImpl();
    }

    @Bean
    public QuizService quizService(QuestionDao questionDao) {
        return new QuizServiceImpl(questionDao);
    }

    @Bean
    public QuizController quizController(QuizService quizService, MessageSource messageSource) {
        return new QuizController(quizService, messageSource);
    }

    @Bean("messageSource")
    public MessageSource getMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("i18n/QuizBundle");
        return messageSource;
    }
}