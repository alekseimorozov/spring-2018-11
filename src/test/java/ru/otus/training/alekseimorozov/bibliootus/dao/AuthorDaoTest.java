package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("repository AuthorDao")
class AuthorDaoTest extends CommonDaoJpaTest {
    @Autowired
    private AuthorDao authorDao;

    @Test
    @DisplayName("returns list of Authors by part of fullName")
    public void findByFullNameContainingIgnoreCase() {
        Author first = getEntityManager().find(Author.class, 3L);
        Author second = getEntityManager().find(Author.class, 4L);
        assertThat(authorDao.findByFullNameContainingIgnoreCase("иЙ")).containsOnly(first, second);
    }

    @Test
    @DisplayName("returns empty list of Authors if by part of fullName not found")
    public void findByFullNameContainingIgnoreCaseNotFound() {
        assertThat(authorDao.findByFullNameContainingIgnoreCase("ZZ")).hasSize(0);
    }

    @Test
    @DisplayName("returns list of Authors for book with required id ")
    public void findByBookIdTest() {
        Author first = getEntityManager().find(Author.class, 2L);
        Author second = getEntityManager().find(Author.class, 3L);
        assertThat(authorDao.findByBookId(2L)).containsOnly(first, second);
    }

    @Test
    @DisplayName("returns empty list of Authors if book with required id not found")
    public void findByBookIdTestNotFound() {
        assertThat(authorDao.findByBookId(-1L)).hasSize(0);
    }
}