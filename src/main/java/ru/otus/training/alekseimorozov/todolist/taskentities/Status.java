package ru.otus.training.alekseimorozov.todolist.taskentities;

public enum Status {
    NEW("new"), INPROGRESS("in progress"), PENDING("pending"), DONE("done"), DICLINE("dicline");
    public static final Status[] ALL = {NEW, INPROGRESS, PENDING, DONE, DICLINE};
    private final String name;

    Status(String name) {
        this.name = name;
    }
}