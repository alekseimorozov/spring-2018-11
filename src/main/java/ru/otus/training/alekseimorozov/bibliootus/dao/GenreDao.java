package ru.otus.training.alekseimorozov.bibliootus.dao;

import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.List;

public interface GenreDao {
    Genre create(Genre genre);

    List<Genre> readAll();

    Genre readById(Long genreId);

    void update(Long genreId, String name);

    void delete(Long genreId);
}