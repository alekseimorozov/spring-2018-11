package ru.otus.training.alekseimorozov.quiz.entity;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private Integer id;
    private String content;
    private String correctAnswerId;
    private List<Answer> answers = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        verifyId(id);
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        verifyContent(content);
        this.content = content;
    }

    public String getCorrectAnswerId() {
        return correctAnswerId;
    }

    public void setCorrectAnswerId(String correctAnswerId) {
        verifyCorrectAnswerId(correctAnswerId);
        this.correctAnswerId = correctAnswerId;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        verifyAnswers(answers);
        this.answers = answers;
    }

    private void verifyId(Integer id) {
        if (id == null) {
            throwQuizException("wrong question id (null)");
        }
    }

    private void verifyContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throwQuizException("Wrong question content (empty or null)");
        }
    }

    private void verifyCorrectAnswerId(String correctAnswerId) {
        if (correctAnswerId == null || correctAnswerId.trim().isEmpty()) {
            throwQuizException("Wrong question correctAnswerId (empty or null)");
        }
    }

    private void verifyAnswers(List<Answer> answers) {
        if (answers == null || answers.size() == 0) {
            throwQuizException("wrong question answers list (empty or null)");
        }
    }

    private void throwQuizException(String message) {
        throw new QuizException(message);
    }

    @Override
    public String toString() {
        return id + " " + content;
    }
}