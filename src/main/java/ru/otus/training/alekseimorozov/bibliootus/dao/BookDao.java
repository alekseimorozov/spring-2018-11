package ru.otus.training.alekseimorozov.bibliootus.dao;

import ru.otus.training.alekseimorozov.bibliootus.entity.Book;

import java.util.List;

public interface BookDao {
    Book create(Book testBook);

    List<Book> readAll();

    Book readById(Long id);

    List<Book> findByName(String name);

    List<Book> findByAuthorName(String name);

    List<Book> findByAuthorId(Long id);

    List<Book> findByGenreId(Long id);

    void updateBookName(Long id, String name);

    void updateBookGenre(Long bookId, Long genreId);

    void addAuthorToBook(Long bookId, Long authorId);

    void removeAuthorFromBook(Long bookId, Long authorId);

    void delete(Long id);
}