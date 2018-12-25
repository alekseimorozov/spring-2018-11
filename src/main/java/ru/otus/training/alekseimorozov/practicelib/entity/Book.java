package ru.otus.training.alekseimorozov.practicelib.entity;

import java.util.ArrayList;
import java.util.List;

public class Book extends CommonEntity {
    private static final long serialVersionUID = 1L;

    private List<Author> authors = new ArrayList<>();
    private Long genreId;

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }
}