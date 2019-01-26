package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import ru.otus.training.alekseimorozov.bibliootus.entity.Book;

import java.util.List;

public interface BookService {
    Book create(String title, Long genreId, Long author_id);

    List<Book> readAll();

    Book readById(Long id);

    /**
     * Search books by part of name
     *
     * @param name - part of book name to find
     * @return list of book or empty list
     */
    List<Book> findByName(String name);

    /**
     * Search books which by part of author's name
     *
     * @param authorName - part of authors name to find
     * @return list of books or empty list
     */
    List<Book> findByAuthorName(String authorName);

    /**
     * Search book which has author with id == "authorId"
     *
     * @param authorId
     * @return list of books which was written by author with id = ""authorId
     */
    List<Book> findByAuthorId(Long authorId);

    /**
     * Search book by genreId
     *
     * @param genreId genreId of book to search
     * @return list of book with genre equals "genre" or empty list
     */
    List<Book> findByGenreId(Long genreId);

    void updateBookName(Long bookId, String name);

    void updateBookGenre(Long bookId, Long genreId);

    void addAuthorToBook(Long bookId, Long authorId);

    void removeAuthorFromBook(Long bookId, Long authorId);

    void delete(Long bookId);
}