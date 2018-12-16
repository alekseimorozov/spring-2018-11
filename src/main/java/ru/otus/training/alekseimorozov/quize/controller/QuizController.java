package ru.otus.training.alekseimorozov.quize.controller;

import static java.lang.System.out;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import ru.otus.training.alekseimorozov.quize.entity.Answer;
import ru.otus.training.alekseimorozov.quize.entity.Question;
import ru.otus.training.alekseimorozov.quize.entity.QuizException;
import ru.otus.training.alekseimorozov.quize.service.QuizService;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
public class QuizController {
    private QuizService quizService;
    private MessageSource messageSource;

    public QuizController(QuizService quizService, MessageSource messageSource) {
        this.quizService = quizService;
        this.messageSource = messageSource;
    }

    public void runQuiz() {
        Scanner scanner = new Scanner(System.in);
        Locale quizLocale = getLocale(scanner);
        out.println(getMessage("entername", quizLocale));
        String name = scanner.nextLine();
        List<Question> questions = quizService.getQuizQuestions(getMessage("file", quizLocale));
        Map<Question, String> testResults = getAnswersToQuiz(questions, scanner, quizLocale);
        showQuizResults(testResults, name, quizLocale);
    }

    private Locale getLocale(Scanner scanner) {
        ResourceBundle languages = ResourceBundle.getBundle("languages");
        Map<String, Locale> locales = new HashMap<>();
        out.println("Choose language:");
        for (String key : languages.keySet()) {
            String[] language = languages.getString(key).split(",");
            locales.put(key, Locale.forLanguageTag(convertIsoToUTF8(language[1].trim())));
            out.println(key + ") " + convertIsoToUTF8(language[0]));
        }
        return locales.get(scanner.nextLine());
    }

    private Map<Question, String> getAnswersToQuiz(List<Question> questions, Scanner scanner, Locale choozenlocale) {
        Map<Question, String> answers = new TreeMap<>(Comparator.comparing(Question::getId));
        for (Question question : questions) {
            out.println(question);
            for (Answer answer : question.getAnswers()) {
                out.println(answer);
            }
            out.println(getMessage("enteranswer", choozenlocale));
            answers.put(question, scanner.nextLine());
        }
        return answers;
    }

    private void showQuizResults(Map<Question, String> answers, String studentName, Locale choosenLocale) {
        int quizResult = 0;
        for (Map.Entry<Question, String> result : answers.entrySet()) {
            out.println(result.getKey());
            out.print(getMessage("correctanswer", choosenLocale) + " " + result.getKey().getCorrectAnswerId() + ", ");
            out.println(getMessage("youranswer", choosenLocale) + " " + result.getValue());
            if (result.getKey().getCorrectAnswerId().equalsIgnoreCase(result.getValue())) {
                quizResult++;
            }
        }
        out.println();
        out.println(getMessage("result", studentName, choosenLocale) + " " + quizResult);
    }

    private String getMessage(String key, Locale choozenLocale) {
        return getMessage(key, null, choozenLocale);
    }

    private String getMessage(String key, String name, Locale choozenLocale) {
        return convertIsoToUTF8(messageSource.getMessage(key, new String[] {name}, "", choozenLocale));
    }

    private String convertIsoToUTF8(String initial) {
        try {
            return new String(initial.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            throw new QuizException("Error during properties file reading", e);
        }
    }
}