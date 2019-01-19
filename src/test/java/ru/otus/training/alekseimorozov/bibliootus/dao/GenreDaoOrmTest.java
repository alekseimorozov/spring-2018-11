package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("class GenreDaoOrm")
class GenreDaoOrmTest extends CommonDaoOrmTest {
    @Autowired
    private GenreDao genreDao;

    @Test
    @DisplayName("save new genre in database")
    void create() {
        Genre expected = new Genre();
        expected.setName("TEST");
        genreDao.create(expected);
        assertThat(getEntityManager().find(Genre.class, expected.getId())).isEqualTo(expected);
    }

    @Test
    @DisplayName("returns list of all genres")
    void readAll() {
        List<Genre> expected = new ArrayList<>();
        expected.add(createGenre("ДЕТСКАЯ"));
        expected.add(createGenre("САТИРА"));
        expected.add(createGenre("ТЕХНИЧЕСКАЯ"));
        assertThat(genreDao.readAll()).isEqualTo(expected);
    }

    @Test
    @DisplayName("returns genre with required id")
    void readById() {
        Genre expected = createGenre("ДЕТСКАЯ");
        assertThat(genreDao.readById(expected.getId())).isEqualTo(expected);
    }

    @Test
    @DisplayName("return new Genre() when reading by not existing id")
    void readByNotExistingId() {
        assertThat(genreDao.readById(77L)).isEqualTo(new Genre());
    }

    @Test
    @DisplayName("updates name of genre")
    void update() {
        Genre expected = createGenre("ДЕТСКАЯ");
        expected.setName("SCIENCE");
        genreDao.update(expected);
        assertThat(getEntityManager().find(Genre.class, expected.getId())).isEqualTo(expected);
    }

    @Test
    @DisplayName("deletes genre with required id")
    void delete() {
        Genre expected = createGenre("ДЕТСКАЯ");
        genreDao.delete(expected.getId());
        assertThat(getEntityManager().find(Genre.class, expected.getId())).isNull();
    }

    @Test
    @DisplayName("handle genre delete operation with not existing id")
    void deleteGenreWhichNotExists() {
        genreDao.delete(77L);
        assertThat(getEntityManager().find(Genre.class, 77L)).isNull();
    }


    @Test
    @DisplayName("throws exception when try to delete genre which linked to some books")
    void deleteForeignKey() {
        Book book = new Book();
        book.setTitle("Test");
        Genre expected = createGenre("ДЕТСКАЯ");
        book.setGenre(expected);
        genreDao.delete(expected.getId());
        assertThatExceptionOfType(DataIntegrityViolationException.class);
    }

    private Genre createGenre(String name) {
        Genre genre = getEntityManager().persistAndFlush(new Genre(name));
        getEntityManager().flush();
        getEntityManager().detach(genre);
        return genre;
    }
}