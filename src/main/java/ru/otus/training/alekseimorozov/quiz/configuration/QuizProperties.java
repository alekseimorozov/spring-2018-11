package ru.otus.training.alekseimorozov.quiz.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.*;

@ConfigurationProperties("quiz")
public class QuizProperties {
    private Map<String, String> sources = new HashMap<>();
    private List<List<String>> languages = new ArrayList<>();

    public Map<String, String> getSources() {
        return sources;
    }

    public void setSources(Map<String, String> sources) {
        this.sources = sources;
    }

    public List<List<String>> getLanguages() {
        return languages;
    }

    public void setLanguages(List<List<String>> languages) {
        this.languages = languages;
    }

    public String getQuizSource(Locale locale) {
        String quizSource = sources.get(locale.toString());
        return quizSource == null ? sources.get("default") : quizSource;
    }
}