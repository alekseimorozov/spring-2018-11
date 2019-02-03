package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import ru.otus.training.alekseimorozov.bibliootus.entity.Author;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;

import java.util.List;

public interface BookService {
    Book create(String title, String genreId, String author_id);

    List<Book> readAll();

    Book readById(String id);

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
    List<Book> findByAuthorId(String authorId);

    /**
     * Search book by genreId
     *
     * @param genreId genreId of book to search
     * @return list of book with genre equals "genre" or empty list
     */
    List<Book> findByGenreId(String genreId);

    /**
     * Search all authors of book
     *
     * @param bookId
     * @return list of authors of book or empty list
     */
    List<Author> findAuthorByBook(String bookId);

    void updateBookName(String bookId, String name);

    void updateBookGenre(String bookId, String genreId);

    void addAuthorToBook(String bookId, String authorId);

    void removeAuthorFromBook(String bookId, String authorId);

    void delete(String bookId);

    void deleteComment(String bookId, int commentId);

    void addComment(String bookId, String comment);

    List<String> readCommentsByBookId(String id);

    void updateComment(String bookId, int commentId, String text);
}