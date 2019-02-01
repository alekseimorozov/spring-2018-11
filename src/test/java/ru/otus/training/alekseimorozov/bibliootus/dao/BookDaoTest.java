package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("repository BookDao")
class BookDaoTest extends CommonDaoMongoTest {
    @Autowired
    private BookDao bookDao;


    @BeforeEach
    public void start() {
        firstAuthor = getMongoTemplate().save(new Author("First Author"));
        secondAuthor = getMongoTemplate().save(new Author("Second Author"));
        thirdAuthor = getMongoTemplate().save(new Author("Third Author"));
        fourthAuthor = getMongoTemplate().save(new Author("Fourth Author"));
        firstGenre = getMongoTemplate().save(new Genre("First Genre"));
        secondGenre = getMongoTemplate().save(new Genre("Second Genre"));
        firstBook = new Book();
        firstBook.setTitle("First book");
        firstBook.getAuthors().add(firstAuthor);
        firstBook.getAuthors().add(secondAuthor);
        firstBook.setGenre(secondGenre);
        getMongoTemplate().save(firstBook);
        secondBook = new Book();
        secondBook.setTitle("Second book");
        secondBook.getAuthors().add(secondAuthor);
        secondBook.getAuthors().add(thirdAuthor);
        secondBook.setGenre(firstGenre);
        getMongoTemplate().save(secondBook);
        thirdBook = new Book();
        thirdBook.setTitle("Third book");
        thirdBook.getAuthors().add(firstAuthor);
        thirdBook.getAuthors().add(thirdAuthor);
        thirdBook.setGenre(firstGenre);
        getMongoTemplate().save(thirdBook);
    }

    @Test
    @DisplayName("returns list of Books which title contains part of name")
    public void findByTitleRegex() {
        assertThat(bookDao.findByTitle("Ir")).containsOnly(firstBook, thirdBook);
    }

    @Test
    @DisplayName("returns empty list of Books if part of name not found")
    public void findByTitleContainingIgnoreCaseNotFound() {
        assertThat(bookDao.findByTitle("ZZ")).hasSize(0);
    }

    @Test
    @DisplayName("finds Books by part of Authors full name")
    public void findBookByAuthorName() {
        assertThat(bookDao.findBookByAuthorName("SeCoNd")).containsOnly(firstBook, secondBook);
    }

    @Test
    @DisplayName("returns empty list of Books if part of Author name not found")
    public void findByAuthorNameNotFound() {
        assertThat(bookDao.findBookByAuthorName("ZZ")).hasSize(0);
    }

    @Test
    @DisplayName("finds Books by Author")
    public void findByAuthors() {
        assertThat(bookDao.findByAuthors(firstAuthor)).containsOnly(firstBook, thirdBook);
    }

    @Test
    @DisplayName("returns empty list of Books if Book with required Author  not found")
    public void findByAuthorIdNotFound() {
        assertThat(bookDao.findByAuthors(fourthAuthor)).hasSize(0);
    }

    @Test
    @DisplayName("finds Books which requiared Genre")
    public void findByGenreId() {
        assertThat(bookDao.findByGenre(firstGenre)).containsOnly(secondBook, thirdBook);
    }
}