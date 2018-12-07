package ru.otus.training.alekseimorozov.quize.entity;

public class Answer {
    private String id;
    private String content;

    public Answer(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return id + ") " + content;
    }
}
