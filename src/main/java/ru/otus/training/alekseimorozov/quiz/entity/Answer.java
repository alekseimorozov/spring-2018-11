package ru.otus.training.alekseimorozov.quiz.entity;

public class Answer {
    private String id;
    private String content;

    public Answer(String id, String content) {
        verifyId(id);
        verifyContent(content);
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    private void verifyId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new QuizException("Wrong answer id (null or empty)");
        }
    }

    private void verifyContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new QuizException("Wrong content of answer (null or empty)");
        }
    }

    @Override
    public String toString() {
        return id + ") " + content;
    }
}