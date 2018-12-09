package ru.otus.training.alekseimorozov;

import static java.lang.System.out;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.training.alekseimorozov.quize.entity.Answer;
import ru.otus.training.alekseimorozov.quize.entity.Question;
import ru.otus.training.alekseimorozov.quize.service.QuizService;
import ru.otus.training.alekseimorozov.quize.service.QuizServiceImpl;

import java.io.IOException;
import java.util.*;

public class QuizApp {
    private QuizService quizService;

    public static void main(String[] args) {
        QuizApp quiz = new QuizApp();
        out.println("Введите ваше имя и фамилию:");
        Scanner scanner = new Scanner(System.in);
        String student = scanner.nextLine();
        try {
            Map<Question, String> studentAnswers = quiz.performQuiz(scanner);
            out.println("\nРезультаты теста студента " + student + ":");
            quiz.showQuizResults(studentAnswers);
        } catch (IOException e) {
            out.println("Ошибка при чтении файла с вопросами");
        }
    }

    public QuizApp() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        this.quizService = context.getBean(QuizServiceImpl.class);
    }

    private Map<Question, String> performQuiz(Scanner scanner) throws IOException {
        List<Question> quizQuestions = quizService.getQuizQuestions();
        Map<Question, String> answers = new TreeMap<>(Comparator.comparing(Question::getId));
        for (Question question : quizQuestions) {
            out.println(question);
            for (Answer answer : question.getAnswers()) {
                out.println(answer);
            }
            out.println("введите ответ:");
            answers.put(question, scanner.nextLine());
        }
        return answers;
    }

    private void showQuizResults(Map<Question, String> answers) {
        int quizResult = 0;
        for (Map.Entry<Question, String> result : answers.entrySet()) {
            System.out.println(result.getKey());
            System.out.print(" правильный ответ: " + result.getKey().getCorrectAnswerId());
            System.out.println(" Ваш ответ: " + result.getValue());
            if (result.getKey().getCorrectAnswerId().equalsIgnoreCase(result.getValue())) {
                quizResult++;
            }
        }
        System.out.println();
        System.out.println("Итоговая оценка за тест: " + quizResult);
    }
}