package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.otus.training.alekseimorozov.bibliootus.entity.Genre.getGenre;

class GenreDaoJdbcTest extends CommonDaoJdbcTest {
    private static final String SELECT_GENRE = "SELECT * FROM GENRES WHERE id = :id AND name = :name";
    private static final String SELECT_THIRD = "SELECT name FROM GENRES WHERE id = 3";

    private GenreDao genreDao;

    @BeforeEach
    public void prepareTable() {
        getJdbc().update("DELETE FROM GENRES WHERE id > 2", new HashMap<>());
        getJdbc().update("INSERT INTO GENRES (ID, NAME) VALUES(3, 'ТЕХНИЧЕСКАЯ')", new HashMap<>());
        genreDao = new GenreDaoJdbc(getJdbc());
    }

    @Test
    void create() {
        Genre expected = new Genre();
        expected.setName("TEST");
        genreDao.create(expected);
        Genre actual = getJdbc().query(SELECT_GENRE, new BeanPropertySqlParameterSource(expected),
                (resultSet) -> resultSet.next() ?
                        getGenre(resultSet.getLong("id"), resultSet.getString("name")) : null);
        assertEquals(expected, actual);
    }

    @Test
    void readAll() {
        List<Genre> expected = new ArrayList<>();
        expected.add(getGenre(1L, "ДЕТСКАЯ"));
        expected.add(getGenre(2L, "САТИРА"));
        expected.add(getGenre(3L, "ТЕХНИЧЕСКАЯ"));
        assertArrayEquals(expected.toArray(), genreDao.readAll().toArray());
    }

    @Test
    void readById() {
        Genre expected = getGenre(1L, "ДЕТСКАЯ");
        assertEquals(expected, genreDao.readById(1L));
    }

    @Test
    void update() {
        String updatedName = "SCIENCE";
        genreDao.update(3L, updatedName);
        assertEquals(updatedName, getJdbc().query(SELECT_THIRD,
                (resultSet) -> resultSet.next() ? resultSet.getString(1) : null));
    }

    @Test
    void delete() {
        genreDao.delete(3L);
        assertNull(getJdbc().query(SELECT_THIRD,
                (resultSet) -> resultSet.next() ? resultSet.getString(1) : null));
    }

    @Test
    void deleteForeignKey() {
        assertThrows(DataIntegrityViolationException.class, () -> genreDao.delete(2L));
    }
}