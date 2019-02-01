package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import ru.otus.training.alekseimorozov.bibliootus.businesslogic.serviceexception.BiblioServiceException;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.List;

public interface GenreService {
    Genre create(String genreName);

    List<Genre> readAll();

    Genre readById(String genreId);

    void update(String genreId, String name);

    void delete(String genreId) throws BiblioServiceException;
}