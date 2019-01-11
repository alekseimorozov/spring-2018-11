package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import ru.otus.training.alekseimorozov.bibliootus.entity.Author;

import java.util.List;

public interface AuthorService {
    Author create(String name);

    List<Author> readAll();

    Author findById(Long id);

    /**
     * Search authors by part of name
     *
     * @param name - defines part of author's name to find
     * @return List of authors or empty List
     */
    List<Author> findByName(String name);

    /**
     * Search all authors of book
     *
     * @param bookId
     * @return list of authors of book or empty list
     */
    List<Author> findByBook(Long bookId);

    void update(Long id, String name);

    void delete(Long authorId);
}