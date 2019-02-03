package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.serviceexception.BiblioServiceException;
import ru.otus.training.alekseimorozov.bibliootus.dao.BookDao;
import ru.otus.training.alekseimorozov.bibliootus.dao.GenreDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("class GenreServiceImpl")
public class GenreServiceImplTest extends CommonServiceTest {
    @MockBean
    private GenreDao genreDao;
    @MockBean
    private BookDao bookDao;
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
        String id = "testId";
        Genre genre = Genre.getGenre(id, "Test");
        when(genreDao.findById(id)).thenReturn(Optional.of(genre));
        genreService.readById(id);
        verify(genreDao).findById(id);
    }

    @Test
    @DisplayName("throws BiblioServiceException if required Genre doesn't exist")
    public void readByIdGenreNotExistsTest() {
        String id = "testId";
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> genreService.readById(id))
                .withMessage(String.format("Genre with id: %s not exists", id));
    }

    @Test
    @DisplayName("get Genre by colling genreDao.findById(), modify and save Genre by colling genreDao.save()")
    public void updateTest() {
        String id = "testId";
        Genre updatedGenre = Genre.getGenre(id, "Test");
        String updatedGenreName = "Updated genre name";
        InOrder inOrder = Mockito.inOrder(genreDao);
        when(genreDao.findById(id)).thenReturn(Optional.of(updatedGenre));

        genreService.update(id, updatedGenreName);
        inOrder.verify(genreDao).findById(id);
        inOrder.verify(genreDao).save(updatedGenre);
        assertThat(updatedGenre.getName()).isEqualTo(updatedGenreName);
    }

    @Test
    @DisplayName("throws BiblioServiceException if try to update not existing Genre")
    public void updateGenreNotExistsTest() {
        String id = "testId";
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> genreService.update(id, "Test"))
                .withMessage(String.format("Genre with id: %s not exists", id));
    }

    @Test
    @DisplayName("get genre by colling genreDao.findById, check if Book with such Genre exists by calling bookDao. " +
            "countBookByGenre() then delete the Genre by calling genreDao.delete() ")
    public void deleteTest() {
        String id = "testId";
        Genre genre = Genre.getGenre(id, "Test Genre");
        List<Book> books = new ArrayList<>();
        InOrder inOrder = Mockito.inOrder(genreDao, bookDao, genreDao);
        when(genreDao.findById(id)).thenReturn(Optional.of(genre));
        when(bookDao.countBookByGenre(genre)).thenReturn(0);

        genreService.delete(id);
        inOrder.verify(genreDao).findById(id);
        inOrder.verify(bookDao).countBookByGenre(genre);
        inOrder.verify(genreDao).deleteById(id);
    }
}