package ru.otus.training.alekseimorozov.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.training.alekseimorozov.quiz.configuration.QuizProperties;
import ru.otus.training.alekseimorozov.quiz.controller.QuizController;

@SpringBootApplication
@EnableConfigurationProperties(QuizProperties.class)
public class Main{
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        context.getBean(QuizController.class).runQuiz();
    }
}