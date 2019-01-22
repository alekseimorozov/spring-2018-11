package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import javax.persistence.PersistenceException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Class AuthorDaoOrm")
class AuthorDaoOrmTest extends CommonDaoOrmTest {
    @Autowired
    private AuthorDao authorDao;

    @Test
    @DisplayName("create() save Author to database")
    void create() {
        Author expectedAuthor = new Author("Test Author");
        authorDao.create(expectedAuthor);
        assertThat(getEntityManager().find(Author.class, expectedAuthor.getId())).isEqualTo(expectedAuthor);
    }

    @Test
    @DisplayName("readAll() returns list of all authors")
    void readAll() {
        Author first = getEntityManager().persistAndFlush(new Author("НИКОЛАЙ НОСОВ"));
        Author second = getEntityManager().persistAndFlush(new Author("ИЛЬЯ ИЛЬФ"));
        Author third = getEntityManager().persistAndFlush(new Author("ЕВГЕНИЙ ПЕТРОВ"));
        Author fourth = getEntityManager().persistAndFlush(new Author("КОРНЕЙ ЧУКОВСКИЙ"));
        detachAll(first, second, third, fourth);
        assertThat(authorDao.readAll()).containsOnly(first, second, third, fourth);
    }

    @Test
    @DisplayName("findById(id) returns Author with that id")
    void findById() {
        Author expected = getEntityManager().persistAndFlush(new Author("НИКОЛАЙ НОСОВ"));
        getEntityManager().detach(expected);
        assertThat(authorDao.findById(expected.getId())).isEqualTo(expected);
    }

    @Test
    @DisplayName("findById(IdNotExists) return new Author")
    void findByIdWhenIdNotExist() {
        assertThat(authorDao.findById(5L)).isEqualTo(new Author());
    }

    @Test
    @DisplayName("findByName(name) returns list of authors which full name contains entered string ")
    void findByName() {
        Author first = getEntityManager().persistAndFlush(new Author("НИКОЛАЙ НОСОВ"));
        Author second = getEntityManager().persistAndFlush(new Author("ЕВГЕНИЙ ПЕТРОВ"));
        Author third = getEntityManager().persistAndFlush(new Author("КОРНЕЙ ЧУКОВСКИЙ"));
        detachAll(first, second, third);
        assertThat(authorDao.findByName("Ов")).containsOnly(first, second, third);
    }

    @Test
    @DisplayName("return empty ArrayList<Author>")
    void findByNameNothingFound() {
        assertThat(authorDao.findByName("ZZ")).isEqualTo(new ArrayList<Author>());
    }

    @Test
    @DisplayName("findByBookId(bookId) returns list of Author of this book")
    void findByBookId() {
        Book book = new Book();
        book.setTitle("Test");
        book.setGenre(getEntityManager().persistAndFlush(new Genre("Test")));
        Author first = getEntityManager().persistAndFlush(new Author("ИЛЬЯ ИЛЬФ"));
        Author second = getEntityManager().persistAndFlush(new Author("ЕВГЕНИЙ ПЕТРОВ"));
        book.getAuthors().add(first);
        book.getAuthors().add(second);
        Long bookId = getEntityManager().persistAndGetId(book, Long.class);
        assertThat(authorDao.findByBookId(bookId)).containsOnly(first, second);
    }

    @Test
    @DisplayName("findByBookId(IdNotExists) returns empty ArrayList<Author>")
    void findByBookIdNothingFound() {
        assertThat(authorDao.findByBookId(22L)).isEqualTo(new ArrayList<Author>());
    }

    @Test
    @DisplayName("full name is updated")
    void update() {
        Author expected = getEntityManager().persistAndFlush(new Author("Самуил Маршак"));
        getEntityManager().detach(expected);
        expected.setFullName("Борис Житков");
        authorDao.update(expected);
        assertThat(getEntityManager().find(Author.class, expected.getId())).isEqualTo(expected);
    }

    @Test
    @DisplayName("Author is deleted")
    void delete() {
        Author author = getEntityManager().persistAndFlush(new Author("Test Author"));
        getEntityManager().detach(author);
        authorDao.delete(author.getId());
        assertThat(getEntityManager().find(Author.class, author.getId())).isNull();
    }

    @Test
    @DisplayName("Throws exception when try to delete Author that is attached to some books")
    void deleteWithForeignKey() {
        Book book = new Book();
        book.setTitle("Test");
        book.setGenre(getEntityManager().persistAndFlush(new Genre("Test")));
        Author author = getEntityManager().persistAndFlush(new Author("ИЛЬЯ ИЛЬФ"));
        book.getAuthors().add(author);
        getEntityManager().persist(book);
        getEntityManager().flush();
        getEntityManager().detach(book);
        assertThatExceptionOfType(PersistenceException.class).isThrownBy(() -> authorDao.delete(author.getId()));
    }

    protected void detachAll(Object... entities) {
        for (Object entity : entities) {
            getEntityManager().detach(entity);
        }
    }
}