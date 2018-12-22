package ru.otus.training.alekseimorozov.quiz.controller;

import static java.lang.System.out;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import ru.otus.training.alekseimorozov.quiz.configuration.QuizProperties;
import ru.otus.training.alekseimorozov.quiz.entity.Answer;
import ru.otus.training.alekseimorozov.quiz.entity.Question;
import ru.otus.training.alekseimorozov.quiz.service.QuizService;

import java.util.*;

@Controller
public class QuizController {
    private QuizService quizService;
    private MessageSource messageSource;
    private QuizProperties quizProperties;

    @Autowired
    public QuizController(QuizService quizService, MessageSource messageSource, QuizProperties quizProperties) {
        this.quizService = quizService;
        this.quizProperties = quizProperties;
        this.messageSource = messageSource;
    }

    public void runQuiz() {
        Scanner scanner = new Scanner(System.in);
        Locale quizLocale;
        quizLocale = getLocale(scanner);
        out.println(messageSource.getMessage("entername", null, quizLocale));
        String name = scanner.nextLine();
        List<Question> questions = quizService.getQuizQuestions(quizProperties.getQuizSource(quizLocale));
        Map<Question, String> testResults = getAnswersToQuiz(questions, scanner, quizLocale);
        showQuizResults(testResults, name, quizLocale);
    }

    private Locale getLocale(Scanner scanner) throws ArrayIndexOutOfBoundsException {
        out.println("Choose language:");
        List<List<String>> languages = quizProperties.getLanguages();
        for (int i = 1; i < languages.size(); i++) {
            out.println(i + ")" + languages.get(i).get(0));
        }
        try {
            int localeIndex = Integer.parseInt(scanner.nextLine());
            return Locale.forLanguageTag(languages.get(localeIndex).get(1));
        } catch (Exception e) {
            out.println("Incorrect input.");
            out.println("Default language was enabled.");
            out.println();
            return Locale.forLanguageTag(languages.get(0).get(1));
        }
    }

    private Map<Question, String> getAnswersToQuiz(List<Question> questions, Scanner scanner, Locale choozenlocale) {
        Map<Question, String> answers = new TreeMap<>(Comparator.comparing(Question::getId));
        for (Question question : questions) {
            out.println(question);
            for (Answer answer : question.getAnswers()) {
                out.println(answer);
            }
            out.println(messageSource.getMessage("enteranswer", null, choozenlocale));
            answers.put(question, scanner.nextLine());
        }
        return answers;
    }

    private void showQuizResults(Map<Question, String> answers, String studentName, Locale choosenLocale) {
        int quizResult = 0;
        for (Map.Entry<Question, String> result : answers.entrySet()) {
            out.println(result.getKey());
            out.print(messageSource.getMessage("correctanswer", null, choosenLocale) + " " + result.getKey().getCorrectAnswerId() + ", ");
            out.println(messageSource.getMessage("youranswer", null, choosenLocale) + " " + result.getValue());
            if (result.getKey().getCorrectAnswerId().equalsIgnoreCase(result.getValue())) {
                quizResult++;
            }
        }
        out.println();
        out.println(messageSource.getMessage("result", new String[]{studentName}, choosenLocale) + " " + quizResult);
    }
}