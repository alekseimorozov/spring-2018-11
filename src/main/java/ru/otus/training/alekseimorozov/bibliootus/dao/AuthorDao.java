package ru.otus.training.alekseimorozov.bibliootus.dao;

import ru.otus.training.alekseimorozov.bibliootus.entity.Author;

import java.util.List;

public interface AuthorDao {
    void create(Author author);

    List<Author> readAll();

    Author findById(Long id);

    List<Author> findByName(String name);

    List<Author> findByBookId(Long bookId);

    void update(Long id, String name);

    void delete(Long authorId);
}