package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import ru.otus.training.alekseimorozov.bibliootus.CommonTest;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.otus.training.alekseimorozov.bibliootus.entity.Author.getAuthor;
import static ru.otus.training.alekseimorozov.bibliootus.entity.Genre.getGenre;

class BookDaoJdbcTest extends CommonTest {
    private static final String SELECT_BOOK = "SELECT b.id as id, b.name as name, g.id as genre_id, g.name as " +
            "genre_name FROM BOOKS b JOIN GENRES g ON g.id = b.genre_id WHERE b.id = :id AND b.name = :name";
    private static final String SELECT_AUTHOR = "SELECT * FROM AUTHOR_TO_BOOK_MAP WHERE book_id = :id";

    @Autowired
    private BookDao bookDao;
    private ResultSetExtractor<Book> bookResultSetExtractor = (resultSet) -> {
        Book book = new Book();
        if (resultSet.next()) {
            book.setId(resultSet.getLong("id"));
            book.setName(resultSet.getString("name"));
            book.setGenre(getGenre(resultSet.getLong("genre_id"), resultSet.getString("genre_name")));
        }
        return book;
    };
    private Genre childLib = getGenre(1L, "ДЕТСКАЯ");
    private Genre satire = getGenre(2L, "САТИРА");


    @BeforeEach
    public void prepareTable() {
        getJdbc().update("DELETE FROM AUTHOR_TO_BOOK_MAP WHERE book_id > 1", new HashMap<>());
        getJdbc().update("DELETE FROM BOOKS WHERE id > 1", new HashMap<>());
        getJdbc().update("INSERT INTO PUBLIC.BOOKS (ID, NAME, GENRE_ID) VALUES(2, 'ЗОЛОТОЙ ТЕЛЕНОК', 2)", new HashMap<>());
        getJdbc().update("INSERT INTO PUBLIC.AUTHOR_TO_BOOK_MAP(AUTHOR_ID, BOOK_ID) VALUES(2, 2)", new HashMap<>());
        getJdbc().update("INSERT INTO PUBLIC.AUTHOR_TO_BOOK_MAP(AUTHOR_ID, BOOK_ID) VALUES(3, 2)", new HashMap<>());
    }

    @Test
    void create() {
        Book testBook = new Book();
        testBook.setName("ТАРАКАНИЩЕ");
        testBook.setGenre(childLib);
        List<Author> authors = new ArrayList<>();
        Long expectedAuthorId = 4L;
        authors.add(getAuthor(expectedAuthorId, "КАРНЕЙ ЧУКОВСКИЙ"));
        testBook.setAuthors(authors);
        bookDao.create(testBook);
        Book actualBook = getJdbc().query(SELECT_BOOK, new BeanPropertySqlParameterSource(testBook),
                bookResultSetExtractor);
        assertEquals(testBook.getId(), actualBook.getId());
        assertEquals(testBook.getName(), actualBook.getName());
        assertEquals(testBook.getGenre(), actualBook.getGenre());
        Long actualAuthorId = getJdbc().query(SELECT_AUTHOR, new BeanPropertySqlParameterSource(testBook),
                (resultSet -> resultSet.next() ? resultSet.getLong("author_id") : -1));
        assertEquals(expectedAuthorId, actualAuthorId);
    }

    @Test
    void readAll() {
        Book first = new Book();
        first.setId(1L);
        first.setName("ЗАТЕЙНИКИ");
        first.setGenre(childLib);
        Book second = new Book();
        second.setId(2L);
        second.setName("ЗОЛОТОЙ ТЕЛЕНОК");
        second.setGenre(satire);
        List<Book> expected = new ArrayList<>();
        expected.add(first);
        expected.add(second);
        assertEquals(expected, bookDao.readAll());
    }

    @Test
    void readById() {
        Book expected = new Book();
        expected.setId(1L);
        expected.setName("ЗАТЕЙНИКИ");
        expected.setGenre(childLib);
        assertEquals(expected, bookDao.readById(1L));
    }

    @Test
    void readByIdNotExist() {
        Book expected = new Book();
        assertEquals(expected, bookDao.readById(10L));
    }

    @Test
    void findByName() {
        Book book = new Book();
        book.setId(1L);
        book.setName("ЗАТЕЙНИКИ");
        book.setGenre(childLib);
        List<Book> expected = new ArrayList<>();
        expected.add(book);
        assertArrayEquals(expected.toArray(), bookDao.findByName("за").toArray());
    }

    @Test
    void findByNameNotFound() {
        assertArrayEquals(new ArrayList<Book>().toArray(), bookDao.findByName("ZZ").toArray());
    }

    @Test
    void findByAuthorName() {
        Book book = new Book();
        book.setId(1L);
        book.setName("ЗАТЕЙНИКИ");
        book.setGenre(childLib);
        List<Book> expected = new ArrayList<>();
        expected.add(book);
        assertArrayEquals(expected.toArray(), bookDao.findByAuthorName("ос").toArray());
    }

    @Test
    void findByAuthorNameNotFound() {
        assertArrayEquals(new ArrayList<Book>().toArray(), bookDao.findByAuthorName("ZZ").toArray());
    }

    @Test
    void findByAuthorId() {
        Book book = new Book();
        book.setId(1L);
        book.setName("ЗАТЕЙНИКИ");
        book.setGenre(childLib);
        List<Book> expected = new ArrayList<>();
        expected.add(book);
        assertArrayEquals(expected.toArray(), bookDao.findByAuthorId(1L).toArray());
    }

    @Test
    void findByGenreId() {
        Book book = new Book();
        book.setId(1L);
        book.setName("ЗАТЕЙНИКИ");
        book.setGenre(childLib);
        List<Book> expected = new ArrayList<>();
        expected.add(book);
        assertArrayEquals(expected.toArray(), bookDao.findByGenreId(1L).toArray());
    }

    @Test
    void updateBookName() {
        bookDao.updateBookName(2L, "12 СТУЛЬЕВ");
        assertEquals("12 СТУЛЬЕВ", getJdbc().query("SELECT NAME FROM BOOKS WHERE ID = 2",
                resultSet -> resultSet.next() ? resultSet.getString(1) : null));
    }

    @Test
    void updateBookGenre() {
        bookDao.updateBookGenre(2L, 1L);
        Long actualId = getJdbc().query("SELECT GENRE_ID FROM BOOKS WHERE ID = 2", (resultSet) -> resultSet.next() ?
                resultSet.getLong(1) : -1);
        assertEquals(Optional.of(1L), Optional.of(actualId));
    }

    @Test
    void addAuthorToBook() {
        bookDao.addAuthorToBook(2L, 1L);
        Long actualAuthor =
                getJdbc().query("SELECT author_id FROM AUTHOR_TO_BOOK_MAP WHERE book_id = 2 " +
                        "AND author_id  = 1", resultSet -> resultSet.next() ? resultSet.getLong(1) : -1);
        assertEquals(Optional.of(1L), Optional.of(actualAuthor));
    }

    @Test
    void addAuthorToBookDuplicateAuthor() {
        assertThrows(DuplicateKeyException.class, () -> bookDao.addAuthorToBook(2L, 2L));
    }

    @Test
    void removeAuthorFromBook() {
        bookDao.removeAuthorFromBook(2L, 2L);
        assertNull(getJdbc().query("SELECT author_id FROM AUTHOR_TO_BOOK_MAP WHERE book_id = 2 AND author_id = 2",
                resultSet -> resultSet.next() ? resultSet.getLong(1) : null));
    }

    @Test
    void delete() {
        bookDao.delete(2L);
        assertNull(getJdbc().query("SELECT * FROM BOOKS WHERE id = 2",
                resultSet -> resultSet.next() ? resultSet.getLong("id") : null));
        assertNull(getJdbc().query("SELECT * FROM AUTHOR_TO_BOOK_MAP WHERE book_id = 2",
                resultSet -> resultSet.next() ? resultSet.getLong("book_id") : null));
    }
}