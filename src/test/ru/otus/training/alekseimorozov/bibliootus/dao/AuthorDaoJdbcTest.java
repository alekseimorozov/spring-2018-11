package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static ru.otus.training.alekseimorozov.bibliootus.entity.Author.getAuthor;

class AuthorDaoJdbcTest extends CommonDaoJdbcTest {
    private static final String SELECT_AUTHOR = "SELECT * FROM AUTHORS WHERE id = :id and full_name = :fullName";
    private static final String SELECT_NAME = "SELECT full_name FROM AUTHORS WHERE id = :id";
    private static final String SELECT_BY_ID = "SELECT * FROM AUTHORS WHERE id = :id";
    private static final String INSERT_AUTHOR = "INSERT INTO AUTHORS (ID, FULL_NAME) VALUES(:id, :fullName)";
    private static final String INSERT_AUTHOR_BOOK_MAP = "INSERT INTO AUTHOR_TO_BOOK_MAP(AUTHOR_ID, BOOK_ID) " +
            "VALUES(:authorId, :bookId)";
    private static final String CLEAR_TABLES = "DELETE FROM AUTHOR_TO_BOOK_MAP; DELETE FROM AUTHORS";

    private AuthorDao authorDao;

    @BeforeEach
    public void prepareTables() {
        getJdbc().update(CLEAR_TABLES, new HashMap<>());
        getJdbc().batchUpdate(INSERT_AUTHOR, new SqlParameterSource[]{
                new BeanPropertySqlParameterSource(getAuthor(1L, "НИКОЛАЙ НОСОВ")),
                new BeanPropertySqlParameterSource(getAuthor(2L, "ИЛЬЯ ИЛЬФ")),
                new BeanPropertySqlParameterSource(getAuthor(3L, "ЕВГЕНИЙ ПЕТРОВ")),
                new BeanPropertySqlParameterSource(getAuthor(4L, "КОРНЕЙ ЧУКОВСКИЙ"))
        });
        Map<String, Long>[] params = new HashMap[]{
                new HashMap(), new HashMap()
        };
        params[0].put("authorId", 2L);
        params[0].put("bookId", 2L);
        params[1].put("authorId", 3L);
        params[1].put("bookId", 2L);
        getJdbc().batchUpdate(INSERT_AUTHOR_BOOK_MAP, params);
        authorDao = new AuthorDaoJdbc(getJdbc());
    }

    @Test
    void create() {
        Author expectedAuthor = new Author();
        expectedAuthor.setFullName("Author Test");
        authorDao.create(expectedAuthor);
        SqlParameterSource params = new BeanPropertySqlParameterSource(expectedAuthor);
        Author actualAuthor = getJdbc().queryForObject(SELECT_AUTHOR, params, (resultSet, i) ->
                getAuthor(resultSet.getLong("id"), resultSet.getString("full_name")));
        assertEquals(expectedAuthor, actualAuthor);
    }

    @Test
    void readAll() {
        List<Author> actual = authorDao.readAll();
        List<Author> expected = new ArrayList<>();
        expected.add(getAuthor(1L, "НИКОЛАЙ НОСОВ"));
        expected.add(getAuthor(2L, "ИЛЬЯ ИЛЬФ"));
        expected.add(getAuthor(3L, "ЕВГЕНИЙ ПЕТРОВ"));
        expected.add(getAuthor(4L, "КОРНЕЙ ЧУКОВСКИЙ"));
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void findById() {
        Author actual = authorDao.findById(1L);
        Author expected = getAuthor(1L, "НИКОЛАЙ НОСОВ");
        assertEquals(expected, actual);
    }

    @Test
    void findByIdWhenIdNotExist() {
        Author actual = authorDao.findById(5L);
        Author expected = new Author();
        assertEquals(expected, actual);
    }

    @Test
    void findByName() {
        List<Author> actual = authorDao.findByName("Ов");
        List<Author> expected = new ArrayList<>();
        expected.add(getAuthor(1L, "НИКОЛАЙ НОСОВ"));
        expected.add(getAuthor(3L, "ЕВГЕНИЙ ПЕТРОВ"));
        expected.add(getAuthor(4L, "КОРНЕЙ ЧУКОВСКИЙ"));
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void findByNameNothingFound() {
        List<Author> actual = authorDao.findByName("ZZ");
        List<Author> expected = new ArrayList<>();
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void findByBookId() {
        List<Author> actual = authorDao.findByBookId(2L);
        List<Author> expected = new ArrayList<>();
        expected.add(getAuthor(2L, "ИЛЬЯ ИЛЬФ"));
        expected.add(getAuthor(3L, "ЕВГЕНИЙ ПЕТРОВ"));
        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    void findByBookIdNothingFound() {
        List<Author> actual = authorDao.findByBookId(22L);
        assertArrayEquals(new Author[]{}, actual.toArray());
    }

    @Test
    void update() {
        String expectedAuthorName = "Самуил Маршак";
        Long idToUpdate = 4L;
        authorDao.update(idToUpdate, expectedAuthorName);
        Map<String, Long> params = new HashMap<>();
        params.put("id", idToUpdate);
        String actualAuthorName = getJdbc().queryForObject(SELECT_NAME, params, String.class);
        assertEquals(expectedAuthorName, actualAuthorName);
    }

    @Test
    void delete() {
        Long idToDelete = 4L;
        authorDao.delete(idToDelete);
        Map<String, Long> params = new HashMap<>();
        params.put("id", idToDelete);
        assertNull(getJdbc().query(SELECT_BY_ID, params, resultSet -> resultSet.next() ? new Author() : null));
    }

    @Test
    void deleteWithForeignKey() {
        assertThrows(DataIntegrityViolationException.class, () -> authorDao.delete(2L));
    }
}