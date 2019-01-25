package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.serviceexception.BiblioServiceException;
import ru.otus.training.alekseimorozov.bibliootus.dao.GenreDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("class GenreServiceImpl")
public class GenreServiceImplTest extends CommonServiceTest {
    @MockBean
    private GenreDao genreDao;
    @Autowired
    private GenreService genreService;

    @Test
    @DisplayName("create new Genre and save it by calling genreDao.save()")
    public void createTest() {
        String testGenreName = "Test";
        Genre genre = new Genre(testGenreName);

        genreService.create(testGenreName);
        verify(genreDao).save(genre);
    }

    @Test
    @DisplayName("calls genreDao.findAll() to get all Genre from DB")
    public void readAllTest() {
        genreService.readAll();
        verify(genreDao).findAll();
    }

    @Test
    @DisplayName("calls genreDao.findById() to get from DB Genre with required ID")
    public void readByIdTest() {
        Genre genre = Genre.getGenre(1L, "Test");
        when(genreDao.findById(1L)).thenReturn(Optional.of(genre));
        genreService.readById(1l);
        verify(genreDao).findById(1L);
    }

    @Test
    @DisplayName("throws BiblioServiceException if required Genre doesn't exist")
    public void readByIdGenreNotExistsTest() {
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> genreService.readById(1L))
                .withMessage(String.format("Genre with id: %d not exists", 1L));
    }

    @Test
    public void updateTest() {
        Genre updatedGenre = Genre.getGenre(1L, "Test");
        when(genreDao.findById(1L)).thenReturn(Optional.of(updatedGenre));
        InOrder inOrder = Mockito.inOrder(genreDao);

        genreService.update(1L, "Test");
        inOrder.verify(genreDao).findById(1L);
        inOrder.verify(genreDao).save(updatedGenre);
    }

    @Test
    @DisplayName("throws BiblioServiceException if try to update not existing Genre")
    public void updateGenreNotExistsTest() {
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> genreService.update(1L, "Test"))
                .withMessage(String.format("Genre with id: %d not exists", 1L));
    }

    @Test
    public void deleteTest() {
        genreService.delete(1L);
        verify(genreDao).deleteById(1L);
    }
}