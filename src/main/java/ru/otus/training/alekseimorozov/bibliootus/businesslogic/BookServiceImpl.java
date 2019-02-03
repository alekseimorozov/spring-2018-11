package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.serviceexception.BiblioServiceException;
import ru.otus.training.alekseimorozov.bibliootus.dao.AuthorDao;
import ru.otus.training.alekseimorozov.bibliootus.dao.BookDao;
import ru.otus.training.alekseimorozov.bibliootus.dao.GenreDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private BookDao bookDao;
    private AuthorDao authorDao;
    private GenreDao genreDao;

    @Autowired
    public BookServiceImpl(BookDao bookDao, AuthorDao authorDao, GenreDao genreDao) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
    }

    @Override
    public Book create(String title, String genreId, String authorId) {
        Book book = new Book();
        book.setGenre(checkAndReturnGenreIfExists(genreId));
        book.getAuthors().add(checkAndReturnAuthorIfExists(authorId));
        book.setTitle(title);
        return bookDao.save(book);
    }

    @Override
    public List<Book> readAll() {
        return bookDao.findAll();
    }

    @Override
    public Book readById(String id) {
        return checkAndReturnBookIfExists(id);
    }

    @Override
    public List<Book> findByName(String name) {
        return bookDao.findByTitle(name);
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        List<Author> authors = authorDao.findByFullNameIgnoreCase(authorName);
        return bookDao.findByAuthorsIsIn(authors);
    }

    @Override
    public List<Book> findByAuthorId(String authorId) {
        return bookDao.findByAuthors(checkAndReturnAuthorIfExists(authorId));
    }

    @Override
    public List<Book> findByGenreId(String genreId) {
        return bookDao.findByGenre(checkAndReturnGenreIfExists(genreId));
    }

    @Override
    public void updateBookName(String bookId, String name) {
        Book book = checkAndReturnBookIfExists(bookId);
        book.setTitle(name);
        bookDao.save(book);
    }

    @Override
    public List<Author> findAuthorByBook(String bookId) {
        return checkAndReturnBookIfExists(bookId).getAuthors();
    }

    @Override
    public void updateBookGenre(String bookId, String genreId) {
        Book book = checkAndReturnBookIfExists(bookId);
        Genre genre = checkAndReturnGenreIfExists(genreId);
        book.setGenre(genre);
        bookDao.save(book);
    }

    @Override
    public void addAuthorToBook(String bookId, String authorId) {
        Book book = checkAndReturnBookIfExists(bookId);
        book.getAuthors().add(checkAndReturnAuthorIfExists(authorId));
        bookDao.save(book);
    }

    @Override
    public void removeAuthorFromBook(String bookId, String authorId) {
        Book book = checkAndReturnBookIfExists(bookId);
        for (int i = 0; i < book.getAuthors().size(); i++) {
            if (book.getAuthors().get(i).getId().equals(authorId)) {
                book.getAuthors().remove(i);
                break;
            }
        }
        bookDao.save(book);
    }

    @Override
    public void delete(String bookId) {
        try {
            bookDao.deleteById(bookId);
        } catch (EmptyResultDataAccessException e) {
            throw new BiblioServiceException(String.format("Book with id %s do not found", bookId), e);
        }
    }

    @Override
    public void deleteComment(String bookId, int commentId) {
        Book book = checkAndReturnBookIfExists(bookId);
        try {
            book.getComments().remove(commentId);
        } catch (IndexOutOfBoundsException e) {
            throw new BiblioServiceException(String.format("Comment with id: %d does not exist", commentId), e);
        }
        bookDao.save(book);
    }

    @Override
    public void addComment(String bookId, String comment) {
        Book book = checkAndReturnBookIfExists(bookId);
        book.getComments().add(comment);
        bookDao.save(book);
    }

    @Override
    public List<String> readCommentsByBookId(String id) {
        return checkAndReturnBookIfExists(id).getComments();
    }

    @Override
    public void updateComment(String bookId, int commentId, String text) {
        Book book = checkAndReturnBookIfExists(bookId);
        book.getComments().set(commentId, text);
        bookDao.save(book);
    }

    private Genre checkAndReturnGenreIfExists(String genreId) {
        return genreDao.findById(genreId)
                .orElseThrow(() -> new BiblioServiceException(String.format("Genre with id: %s not exists", genreId)));
    }

    private Author checkAndReturnAuthorIfExists(String authorId) {
        return authorDao.findById(authorId).orElseThrow(() ->
                new BiblioServiceException(String.format("Author with id: %s not exists", authorId)));
    }

    private Book checkAndReturnBookIfExists(String bookId) {
        return bookDao.findById(bookId)
                .orElseThrow(() -> new BiblioServiceException(String.format("Book with id: %s not exists", bookId)));
    }
}