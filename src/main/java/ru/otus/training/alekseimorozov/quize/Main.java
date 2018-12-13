package ru.otus.training.alekseimorozov.quize;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.training.alekseimorozov.quize.controller.QuizController;

@ComponentScan
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        QuizController quiz = context.getBean(QuizController.class);
        quiz.runQuiz();
    }
}