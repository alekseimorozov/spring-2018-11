package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public Book create(Book book) {
        return bookDao.save(book);
    }

    @Override
    public List<Book> readAll() {
        return (List<Book>) bookDao.findAll();
    }

    @Override
    public Book readById(Long id) {
        return bookDao.findById(id).get();
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
        Book book = bookDao.findById(bookId).get();
        book.setTitle(name);
        bookDao.save(book);
    }

    @Override
    public void updateBookGenre(Long bookId, Long genreId) {
        Book book = bookDao.findById(bookId).get();
        Genre genre = genreDao.findById(genreId).get();
        book.setGenre(genre);
        bookDao.save(book);
    }

    @Override
    public void addAuthorToBook(Long bookId, Long authorId) {
        Book book = bookDao.findById(bookId).get();
        Author author = authorDao.findById(authorId).get();
        book.getAuthors().add(author);
        bookDao.save(book);
    }

    @Override
    public void removeAuthorFromBook(Long bookId, Long authorId) {
        Book book = bookDao.findById(bookId).get();
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
        bookDao.deleteById(bookId);
    }
}