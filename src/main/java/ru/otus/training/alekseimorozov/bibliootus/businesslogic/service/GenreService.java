package ru.otus.training.alekseimorozov.bibliootus.businesslogic.service;

import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.List;

public interface GenreService {
    Genre create(String genreName);

    List<Genre> readAll();

    Genre readById(Long genreId);

    void update(Long genreId, String name);

    void delete(Long genreId);
}