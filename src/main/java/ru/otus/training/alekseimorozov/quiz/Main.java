package ru.otus.training.alekseimorozov.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.otus.training.alekseimorozov.quiz.configuration.QuizProperties;

@SpringBootApplication
@EnableConfigurationProperties(QuizProperties.class)
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}