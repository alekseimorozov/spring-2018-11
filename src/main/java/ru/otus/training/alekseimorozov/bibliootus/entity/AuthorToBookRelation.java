package ru.otus.training.alekseimorozov.bibliootus.entity;

public class AuthorToBookRelation {
    private Long authorId;
    private Long bookId;

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}