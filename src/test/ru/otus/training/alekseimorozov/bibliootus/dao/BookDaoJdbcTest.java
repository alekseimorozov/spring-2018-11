package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.otus.training.alekseimorozov.bibliootus.entity.Author.getAuthor;
import static ru.otus.training.alekseimorozov.bibliootus.entity.Genre.getGenre;

class BookDaoJdbcTest extends CommonDaoJdbcTest {
    private static final String SELECT_BOOK = "    SELECT b.id as id, b.title as title, g.id as genre_id, g.name as " +
            "genre_name FROM BOOKS b JOIN GENRES g ON g.id = b  .genre_id WHERE b.id = :id AND b.title = :title";
    private static final String SELECT_AUTHOR = "SELECT * FROM AUTHOR_TO_BOOK_MAP WHERE book_id = :id";
    private static Book firstExpected;
    private static Book secondExpected;

    private BookDao bookDao;
    private ResultSetExtractor<Book> bookResultSetExtractor = (resultSet) -> {
        Book book = new Book();
        if (resultSet.next()) {
            book.setId(resultSet.getLong("id"));
            book.setTitle(resultSet.getString("title"));
            book.setGenre(getGenre(resultSet.getLong("genre_id"), resultSet.getString("genre_name")));
        }
        return book;
    };

    @BeforeAll
    public static void prepareExpectedBooks() {
        firstExpected = new Book();
        firstExpected.setId(1L);
        firstExpected.setTitle("ЗАТЕЙНИКИ");
        firstExpected.setGenre(getGenre(1L, "ДЕТСКАЯ"));
        firstExpected.getAuthors().add(getAuthor(1L, "НИКОЛАЙ НОСОВ"));
        secondExpected = new Book();
        secondExpected.setId(2L);
        secondExpected.setTitle("ЗОЛОТОЙ ТЕЛЕНОК");
        secondExpected.setGenre(getGenre(2L, "САТИРА"));
        secondExpected.getAuthors().add(getAuthor(2L,"ИЛЬЯ ИЛЬФ"));
        secondExpected.getAuthors().add(getAuthor(3L,"ЕВГЕНИЙ ПЕТРОВ"));
    }


    @BeforeEach
    public void prepareTable() {
        getJdbc().update("DELETE FROM AUTHOR_TO_BOOK_MAP WHERE book_id > 1", new HashMap<>());
        getJdbc().update("DELETE FROM BOOKS WHERE id > 1", new HashMap<>());
        getJdbc().update("INSERT INTO PUBLIC.BOOKS VALUES(2, 'ЗОЛОТОЙ ТЕЛЕНОК', 2)", new HashMap<>());
        getJdbc().update("INSERT INTO PUBLIC.AUTHOR_TO_BOOK_MAP(AUTHOR_ID, BOOK_ID) VALUES(2, 2)", new HashMap<>());
        getJdbc().update("INSERT INTO PUBLIC.AUTHOR_TO_BOOK_MAP(AUTHOR_ID, BOOK_ID) VALUES(3, 2)", new HashMap<>());
        bookDao = new BookDaoJdbc(getJdbc());
    }

    @Test
    void create() {
        Book testBook = new Book();
        testBook.setTitle("ТАРАКАНИЩЕ");
        testBook.setGenre(getGenre(1L, "ДЕТСКАЯ"));
        Long expectedAuthorId = 4L;
        testBook.getAuthors().add(getAuthor(expectedAuthorId, "КАРНЕЙ ЧУКОВСКИЙ"));
        bookDao.create(testBook);
        Book actualBook = getJdbc().query(SELECT_BOOK, new BeanPropertySqlParameterSource(testBook),
                bookResultSetExtractor);
        assertEquals(testBook.getId(), actualBook.getId());
        assertEquals(testBook.getTitle(), actualBook.getTitle());
        assertEquals(testBook.getGenre(), actualBook.getGenre());
        Long actualAuthorId = getJdbc().query(SELECT_AUTHOR, new BeanPropertySqlParameterSource(testBook),
                (resultSet -> resultSet.next() ? resultSet.getLong("author_id") : -1));
        assertEquals(expectedAuthorId, actualAuthorId);
    }

    @Test
    void readAll() {
        List<Book> expected = new ArrayList<>();
        expected.add(firstExpected);
        expected.add(secondExpected);
        assertEquals(expected, bookDao.readAll());
    }

    @Test
    void readById() {
        assertEquals(firstExpected, bookDao.readById(1L));
    }

    @Test
    void readByIdNotExist() {
        Book expected = new Book();
        assertEquals(expected, bookDao.readById(10L));
    }

    @Test
    void findByName() {
        List<Book> expected = new ArrayList<>();
        expected.add(firstExpected);
        assertArrayEquals(expected.toArray(), bookDao.findByName("за").toArray());
    }

    @Test
    void findByNameNotFound() {
        assertArrayEquals(new ArrayList<Book>().toArray(), bookDao.findByName("ZZ").toArray());
    }

    @Test
    void findByAuthorName() {
        List<Book> expected = new ArrayList<>();
        expected.add(firstExpected);
        assertArrayEquals(expected.toArray(), bookDao.findByAuthorName("ос").toArray());
    }

    @Test
    void findByAuthorNameNotFound() {
        assertArrayEquals(new ArrayList<Book>().toArray(), bookDao.findByAuthorName("ZZ").toArray());
    }

    @Test
    void findByAuthorId() {
        List<Book> expected = new ArrayList<>();
        expected.add(firstExpected);
        assertArrayEquals(expected.toArray(), bookDao.findByAuthorId(1L).toArray());
    }

    @Test
    void findByGenreId() {
        List<Book> expected = new ArrayList<>();
        expected.add(firstExpected);
        assertArrayEquals(expected.toArray(), bookDao.findByGenreId(1L).toArray());
    }

    @Test
    void updateBookName() {
        bookDao.updateBookName(2L, "12 СТУЛЬЕВ");
        assertEquals("12 СТУЛЬЕВ", getJdbc().query("SELECT TITLE FROM BOOKS WHERE ID = 2",
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