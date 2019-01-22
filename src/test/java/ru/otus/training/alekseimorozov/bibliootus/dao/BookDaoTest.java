package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("repository BookDao")
class BookDaoTest extends CommonDaoJpaTest {
    @Autowired
    private BookDao bookDao;

    @Test
    @DisplayName("returns list of Books which title contains part of name")
    public void findByTitleContainingIgnoreCase() {
        Book first = getEntityManager().find(Book.class, 1L);
        Book second = getEntityManager().find(Book.class, 2L);
        assertThat(bookDao.findByTitleContainingIgnoreCase("тЕ")).containsOnly(first, second);
    }

    @Test
    @DisplayName("returns empty list of Books if part of name not found")
    public void findByTitleContainingIgnoreCaseNotFound() {
        assertThat(bookDao.findByTitleContainingIgnoreCase("ZZ")).hasSize(0);
    }

    @Test
    @DisplayName("finds Books by part of Authors full name")
    public void findByAuthorName() {
        Book first = getEntityManager().find(Book.class, 2L);
        Book second = getEntityManager().find(Book.class, 3L);
        assertThat(bookDao.findByAuthorName("ий")).containsOnly(first, second);
    }

    @Test
    @DisplayName("returns empty list of Books if part of Author name not found")
    public void findByAuthorNameNotFound() {
        assertThat(bookDao.findByAuthorName("ZZ")).hasSize(0);
    }

    @Test
    @DisplayName("finds Books by Author id")
    public void findByAuthorId() {
        Book first = getEntityManager().find(Book.class, 1L);
        Book second = getEntityManager().find(Book.class, 4L);
        assertThat(bookDao.findByAuthorId(1L)).containsOnly(first, second);
    }

    @Test
    @DisplayName("returns empty list of Books if Author with required id not found")
    public void findByAuthorIdNotFound() {
        assertThat(bookDao.findByAuthorId(77L)).hasSize(0);
    }

    @Test
    @DisplayName("finds Books which Genre with required id")
    public void findByGenreId() {
        Book first = getEntityManager().find(Book.class, 1L);
        Book second = getEntityManager().find(Book.class, 3L);
        Book third = getEntityManager().find(Book.class, 4L);
        assertThat(bookDao.findByGenreId(1L)).containsOnly(first, second, third);
    }

    @Test
    @DisplayName("returns empty list of Books if Genre with required id not found")
    public void findByGenreNotFound() {
        assertThat(bookDao.findByGenreId(77L)).hasSize(0);
    }
}