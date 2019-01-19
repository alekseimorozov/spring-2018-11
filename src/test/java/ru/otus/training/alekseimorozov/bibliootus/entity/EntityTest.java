package ru.otus.training.alekseimorozov.bibliootus.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EntityTest {
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void authorTest() {
        Author expected = entityManager.persistAndFlush(Author.getAuthor(null, "Test"));
        entityManager.detach(expected);
        assertEquals(expected, entityManager.find(Author.class, expected.getId()));
    }

    @Test
    public void genreTest() {
        Genre expected = entityManager.persistAndFlush(Genre.getGenre(null, "Test"));
        entityManager.detach(expected);
        assertEquals(expected, entityManager.find(Genre.class, expected.getId()));
    }

    @Test
    public void bookTest() {
        Genre testGente = entityManager.persistAndFlush(Genre.getGenre(null, "Test genre"));
        Author first = Author.getAuthor(null, "First");
        Author second = Author.getAuthor(null, "Second");
        Author third = Author.getAuthor(null, "Third");
        Book expected = new Book();
        expected.setTitle("Test");
        expected.setGenre(testGente);
        expected.getAuthors().add(first);
        expected.getAuthors().add(second);
        expected.getAuthors().add(third);
        entityManager.persistAndFlush(expected);
        entityManager.detach(expected);
        Book actual = entityManager.find(Book.class, expected.getId());
        assertEquals(actual, expected);
        assertArrayEquals(actual.getAuthors().toArray(), expected.getAuthors().toArray());
    }

    @Test
    public void bookCommentTest() {
        Book testBook = new Book();
        testBook.setTitle("Test book");
        entityManager.persist(testBook);
        BookComment expected = new BookComment("Test comment");
        expected.setBook(testBook);
        Object id = entityManager.persistAndGetId(expected);
        entityManager.flush();
        entityManager.detach(expected);
        assertEquals(expected, entityManager.find(BookComment.class, id));
    }
}