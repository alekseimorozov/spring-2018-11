package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import ru.otus.training.alekseimorozov.bibliootus.entity.Author;

import java.util.List;

public interface AuthorService {
    Author create(String name);

    List<Author> readAll();

    Author findById(String id);

    /**
     * Search authors by part of name
     *
     * @param name - defines part of author's name to find
     * @return List of authors or empty List
     */
    List<Author> findByName(String name);

    void update(String id, String name);

    void delete(String authorId);
}