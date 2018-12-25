package ru.otus.training.alekseimorozov.quiz.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.training.alekseimorozov.quiz.controller.QuizController;

@ShellComponent
public class QuizShellRunner {
    private static final String LANGUAGE_NOT_CHOSEN = ": language hasn't been chosen yet." +
            "\n\tEnter \"language\" command to choose language.";
    private static final String LANGUAGE_AND_NAME_NOT_SET = ": language and/or name hasn't been set yet." +
            "\n\tEnter \"language\" command to choose language.\n\tEnter \"name\" command to set name.";
    private static final String RESULT_NOT_READY = ": quiz hasn't been done yet. Enter \"start\" command to run quiz.";

    private QuizController quizController;
    private boolean isLocaleChosen;
    private boolean isNameSet;
    private boolean isResultReady;

    @Autowired
    public QuizShellRunner(QuizController quizController) {
        this.quizController = quizController;
    }

    @ShellMethod(value = "choose language for quiz")
    public void language() {
        quizController.setQuizLocale();
        isLocaleChosen = true;
        isResultReady = false;
    }

    @ShellMethod(value = "enter your name")
    public void name(@ShellOption(defaultValue = "") String studentName) {
        if (studentName.isEmpty()) {
            quizController.setName();
        } else {
            quizController.setName(studentName);
        }
        isNameSet = true;
        isResultReady = false;
    }

    @ShellMethod("run quiz")
    public void start() {
        quizController.askQuizQuestions();
        isResultReady = true;
    }

    @ShellMethod(value = "show quiz result")
    public void result() {
        quizController.showQuizResults();
    }

    @ShellMethodAvailability({"name"})
    public Availability checkIfLocaleChosen() {
        return isLocaleChosen ? Availability.available() :
                Availability.unavailable(LANGUAGE_NOT_CHOSEN);
    }

    public Availability startAvailability() {
        return isLocaleChosen && isNameSet ? Availability.available() :
                Availability.unavailable(LANGUAGE_AND_NAME_NOT_SET);
    }

    public Availability resultAvailability() {
        return isResultReady ? Availability.available() :
                Availability.unavailable(RESULT_NOT_READY);
    }
}