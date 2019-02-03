package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("repository AuthorDao")
class AuthorDaoTest extends CommonDaoMongoTest {
    @Autowired
    private AuthorDao authorDao;

    @BeforeEach
    public void start() {
        firstAuthor = new Author("First");
        secondAuthor = new Author("Second");
        thirdAuthor = new Author("Third");
        fourthAuthor = new Author("Fourth");
        fifthAuthor = new Author("Fifth");
        getMongoTemplate().save(firstAuthor);
        getMongoTemplate().save(secondAuthor);
        getMongoTemplate().save(thirdAuthor);
        getMongoTemplate().save(fourthAuthor);
        getMongoTemplate().save(fifthAuthor);
    }

    @Test
    @DisplayName("returns list of Authors by part of fullName")
    public void findByFullNameIgnoreCase() {
        assertThat(authorDao.findByFullNameIgnoreCase("fI")).containsOnly(firstAuthor, fifthAuthor);
    }

    @Test
    @DisplayName("returns empty list of Authors if by part of fullName not found")
    public void findByFullNameContainingIgnoreCaseNotFound() {
        assertThat(authorDao.findByFullNameIgnoreCase("ZZ")).hasSize(0);
    }

    @Test
    @DisplayName("should throw exception when try to delete not existing Author")
    public void deleteNotExistingAuthor() {
        authorDao.deleteById("ii");
    }
}