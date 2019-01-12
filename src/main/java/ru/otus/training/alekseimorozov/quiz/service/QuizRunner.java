package ru.otus.training.alekseimorozov.quiz.service;

import static java.lang.System.out;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.training.alekseimorozov.quiz.configuration.QuizProperties;
import ru.otus.training.alekseimorozov.quiz.entity.Answer;
import ru.otus.training.alekseimorozov.quiz.entity.Question;
import ru.otus.training.alekseimorozov.quiz.service.QuizService;

import java.util.*;

@Service
public class QuizRunner {
    private Scanner scanner;
    private QuizService quizService;
    private MessageSource messageSource;
    private QuizProperties quizProperties;
    private Locale quizLocale;
    private String name;
    private Map<Question, String> answers;


    @Autowired
    public QuizRunner(QuizService quizService, MessageSource messageSource, QuizProperties quizProperties) {
        this.quizService = quizService;
        this.quizProperties = quizProperties;
        this.messageSource = messageSource;
        this.scanner = new Scanner(System.in);
    }

    public void setQuizLocale() {
        out.println("Choose language:");
        List<List<String>> languages = quizProperties.getLanguages();
        for (int i = 1; i < languages.size(); i++) {
            out.println(i + ")" + languages.get(i).get(0));
        }
        try {
            int localeIndex = Integer.parseInt(scanner.nextLine());
            quizLocale = Locale.forLanguageTag(languages.get(localeIndex).get(1));
        } catch (Exception e) {
            out.println("Incorrect input.");
            out.println("Default language was enabled.");
            out.println();
            quizLocale = Locale.forLanguageTag(languages.get(0).get(1));
        }
    }

    public void setName() {
        out.println(messageSource.getMessage("entername", null, quizLocale));
        name = scanner.nextLine();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void askQuizQuestions() {
        List<Question> questions = quizService.getQuizQuestions(quizProperties.getQuizSource(this.quizLocale));
        answers = new TreeMap<>(Comparator.comparing(Question::getId));
        for (Question question : questions) {
            out.println(question);
            for (Answer answer : question.getAnswers()) {
                out.println(answer);
            }
            out.println(messageSource.getMessage("enteranswer", null, quizLocale));
            answers.put(question, scanner.nextLine());
        }
    }

    public void showQuizResults() {
        int quizResult = 0;
        for (Map.Entry<Question, String> result : answers.entrySet()) {
            out.println(result.getKey());
            out.print(messageSource.getMessage("correctanswer", null, quizLocale) + " " + result.getKey().getCorrectAnswerId() + ", ");
            out.println(messageSource.getMessage("youranswer", null, quizLocale) + " " + result.getValue());
            if (result.getKey().getCorrectAnswerId().equalsIgnoreCase(result.getValue())) {
                quizResult++;
            }
        }
        out.println();
        out.println(messageSource.getMessage("result", new String[]{name}, quizLocale) + " " + quizResult);
    }
}