package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.training.alekseimorozov.bibliootus.dao.BookDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private BookDao bookDao;

    @Autowired
    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public Book create(Book book) {
        return bookDao.create(book);
    }

    @Override
    public List<Book> readAll() {
        return bookDao.readAll();
    }

    @Override
    public Book readById(Long id) {
        return bookDao.readById(id);
    }

    @Override
    public List<Book> findByName(String name) {
        return bookDao.findByName(name);
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
        bookDao.updateBookName(bookId, name);
    }

    @Override
    public void updateBookGenre(Long bookId, Long genreId) {
        bookDao.updateBookGenre(bookId, genreId);
    }

    @Override
    public void addAuthorToBook(Long bookId, Long authorId) {
        bookDao.addAuthorToBook(bookId, authorId);
    }

    @Override
    public void removeAuthorFromBook(Long bookId, Long authorId) {
        bookDao.removeAuthorFromBook(bookId, authorId);
    }

    @Override
    public void delete(Long bookId) {
        bookDao.delete(bookId);
    }
}