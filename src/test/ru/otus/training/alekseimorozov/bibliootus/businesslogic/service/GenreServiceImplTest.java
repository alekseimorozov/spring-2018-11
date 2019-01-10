package ru.otus.training.alekseimorozov.bibliootus.businesslogic.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.training.alekseimorozov.bibliootus.CommonTest;
import ru.otus.training.alekseimorozov.bibliootus.dao.GenreDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import static org.mockito.Mockito.verify;

public class GenreServiceImplTest extends CommonTest {
    @MockBean
    private GenreDao genreDao;
    @Autowired
    private GenreService genreService;

    @Test
    public void createTest() {
        String testGenreName = "Сатира";
        Genre genre = new Genre();
        genre.setName(testGenreName);
        genreService.create(testGenreName);
        verify(genreDao).create(genre);
    }

    @Test
    public void readAllTest() {
        genreService.readAll();
        verify(genreDao).readAll();
    }

    @Test
    public void readByIdTest() {
        genreService.readById(1l);
        verify(genreDao).readById(1L);
    }

    @Test
    public void updateTest() {
        Long testGenreId = 1L;
        String testGenreName = "Test";
        genreService.update(testGenreId, testGenreName);
        verify(genreDao).update(testGenreId, testGenreName);
    }

    @Test
    public void deleteTest() {
        genreService.delete(1L);
        verify(genreDao).delete(1L);
    }
}