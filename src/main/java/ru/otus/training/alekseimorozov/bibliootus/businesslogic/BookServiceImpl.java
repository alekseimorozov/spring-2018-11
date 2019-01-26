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
    public Book create(String title, Long genreId, Long authorId) {
        Book book = new Book();
        book.setGenre(checkAndReturnGenreIfExists(genreId));
        book.getAuthors().add(checkAndReturnAuthorIfExists(authorId));
        book.setTitle(title);
        return bookDao.save(book);
    }

    @Override
    public List<Book> readAll() {
        return (List<Book>) bookDao.findAll();
    }

    @Override
    public Book readById(Long id) {
        return checkAndReturnBookIfExists(id);
    }

    @Override
    public List<Book> findByName(String name) {
        return bookDao.findByTitleContainingIgnoreCase(name);
    }

    @Override
    public List<Book> findByAuthorName(String author) {
        return bookDao.findByAuthorName(author);
    }

    @Override
    public List<Book> findByAuthorId(Long authorId) {
        return bookDao.findByAuthorId(authorId);
    }

    @Override
    public List<Book> findByGenreId(Long genreId) {
        return bookDao.findByGenreId(genreId);
    }

    @Override
    public void updateBookName(Long bookId, String name) {
        Book book = checkAndReturnBookIfExists(bookId);
        book.setTitle(name);
        bookDao.save(book);
    }

    @Override
    public void updateBookGenre(Long bookId, Long genreId) {
        Book book = checkAndReturnBookIfExists(bookId);
        Genre genre = checkAndReturnGenreIfExists(genreId);
        book.setGenre(genre);
        bookDao.save(book);
    }

    @Override
    public void addAuthorToBook(Long bookId, Long authorId) {
        Book book = checkAndReturnBookIfExists(bookId);
        book.getAuthors().add(checkAndReturnAuthorIfExists(authorId));
        bookDao.save(book);
    }

    @Override
    public void removeAuthorFromBook(Long bookId, Long authorId) {
        Book book = checkAndReturnBookIfExists(bookId);
        for (int i = 0; i < book.getAuthors().size(); i++) {
            if (book.getAuthors().get(i).getId() == authorId) {
                book.getAuthors().remove(i);
                break;
            }
        }
        bookDao.save(book);
    }

    @Override
    public void delete(Long bookId) {
        try {
            bookDao.deleteById(bookId);
        } catch (EmptyResultDataAccessException e) {
            throw new BiblioServiceException(String.format("Book with id %d do not found", bookId), e);
        }
    }

    private Genre checkAndReturnGenreIfExists(Long genreId) {
        return genreDao.findById(genreId)
                .orElseThrow(() -> new BiblioServiceException(String.format("Genre with id: %d not exists", genreId)));
    }

    private Author checkAndReturnAuthorIfExists(Long authorId) {
        return authorDao.findById(authorId).orElseThrow(() ->
                new BiblioServiceException(String.format("Author with id: %d not exists", authorId)));
    }

    private Book checkAndReturnBookIfExists(Long bookId) {
        return bookDao.findById(bookId)
                .orElseThrow(() -> new BiblioServiceException(String.format("Book with id: %d not exists", bookId)));
    }
}