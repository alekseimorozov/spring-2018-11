package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Class BookDaoOrm")
class BookDaoOrmTest extends CommonDaoOrmTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private BookDao bookDao;

    @Test
    @DisplayName("create() save new Book into database")
    void create() {
        Book testBook = new Book();
        testBook.setTitle("ТАРАКАНИЩЕ");
        testBook.setGenre(entityManager.persistAndFlush(new Genre("ДЕТСКАЯ")));
        testBook.getAuthors().add(entityManager.persistAndFlush(new Author("КАРНЕЙ ЧУКОВСКИЙ")));
        bookDao.create(testBook);
        assertThat(testBook).isEqualTo(entityManager.find(Book.class, testBook.getId()));
    }

    @Test
    @DisplayName("readAll() returns list of all books in database")
    void readAll() {
        Book first = createBook("First", "ONE", "Author One", "Author Two");
        Book second = createBook("Second", "TWO", "Author Three", "Author Four");
        assertThat(bookDao.readAll()).containsOnly(first, second);
    }

    @Test
    @DisplayName("readById(id) returns the book with that id")
    void readById() {
        Book first = createBook("First", "ONE", "Author One", "Author Two");
        assertThat(bookDao.readById(first.getId())).isEqualTo(first);
    }

    @Test
    @DisplayName("readById(IdThatNotExist) returns new Book instance")
    void readByIdNotExist() {
        assertThat(bookDao.readById(10L)).isEqualTo(new Book());
    }

    @Test
    @DisplayName("findByName(partOfName) returns books which title has part of name")
    void findByName() {
        Book first = createBook("First", "ONE", "Author One", "Author Two");
        Book second = createBook("Second", "TWO", "Author Three", "Author Four");
        Book third = createBook("Third", "TREE", "Author Three", "Author One");
        assertThat(bookDao.findByName("ir")).containsOnly(first, third);
    }

    @Test
    @DisplayName("findByName(nameNotExist) returns empty ArrayList<book>")
    void findByNameNotFound() {
        assertThat(bookDao.findByName("ZZ")).isEqualTo(new ArrayList<Book>());
    }

    @Test
    @DisplayName("findByAuthorName(partOfName) returns list of books with authors containing partOfName")
    void findByAuthorName() {
        Book first = createBook("First", "ONE", "First");
        Book second = createBook("Second", "TWO", "Second");
        Book third = createBook("Third", "TREE", "Second", "Third");
        Book fours = createBook("Fours", "FOUR", "Third", "Fours");
        assertThat(bookDao.findByAuthorName("IR")).containsOnly(first, third, fours);
    }

    @Test
    @DisplayName("findByAuthorName(nameDosNotExists) returns empty ArrayList<Book>")
    void findByAuthorNameNotFound() {
        assertThat( bookDao.findByAuthorName("ZZ")).isEqualTo(new ArrayList<Book>());
    }

    @Test
    @DisplayName("findByAuthorId(id) returns list of book with Author who has expected id")
    void findByAuthorId() {
        Book first = createBook("First", "ONE", "First");
        Book second = createBook("Second", "TWO", "Second");
        Book third = createBook("Third", "TREE", "Second", "Third");
        Book fours = createBook("Fours", "FOUR", "Third", "Fours");
        Long authorId = second.getAuthors().get(0).getId();
        assertThat(bookDao.findByAuthorId(authorId)).containsOnly(second, third);
    }

    @Test
    @DisplayName("findByGenreId(id) returns list of book with Genre associated to expected id ")
    void findByGenreId() {
        Book first = createBook("First", "ONE", "First");
        Book second = createBook("Second", "TWO", "Second");
        Book third = createBook("Third", "ONE", "Second", "Third");
        Book fours = createBook("Fours", "FOUR", "Third", "Fours");
        Long genreId = first.getGenre().getId();
        assertThat(bookDao.findByGenreId(genreId)).containsOnly(first, third);
    }

    @Test
    @DisplayName("name of book is updated")
    void updateBookName() {
        Book book = createBook("First", "ONE", "First");
        book.setTitle("Second");
        bookDao.update(book);
        assertThat(book).isEqualTo(entityManager.find(Book.class, book.getId()));
    }

    @Test
    @DisplayName("genre of book is updated")
    void updateBookGenre() {
        Book book = createBook("First", "ONE", "First");
        Genre genre = entityManager.persistAndFlush(new Genre("TWO"));
        book.setGenre(genre);
        bookDao.update(book);
        assertThat(genre).isEqualTo(entityManager.find(Book.class, book.getId()).getGenre());
    }

    @Test
    @DisplayName("author is added to the book")
    void addAuthorToBook() {
        Book book = createBook("First", "ONE", "First");
        Author author = entityManager.persistAndFlush(new Author("Second"));
        book.getAuthors().add(author);
        bookDao.update(book);
        assertThat(entityManager.find(Book.class, book.getId()).getAuthors()).hasSize(2).contains(author);
    }

    @Test
    @DisplayName("author is removed from the book")
    void removeAuthorFromBook() {
        Book book = createBook("First", "ONE", "First", "Second", "Third");
        Author first = book.getAuthors().get(0);
        Author second = book.getAuthors().get(1);
        Author third = book.getAuthors().get(2);
        book.getAuthors().remove(1);
        bookDao.update(book);
        assertThat(entityManager.find(Book.class, book.getId()).getAuthors()).containsOnly(first, third);
    }

    @Test
    @DisplayName("book is deleted")
    void delete() {
        Book book = createBook("First", "ONE", "First", "Second", "Third");
        bookDao.delete(book.getId());
        assertThat(entityManager.find(Book.class, book.getId())).isNull();
    }

    private Book createBook(String title, String genreName, String... authorNames) {
        Book book = new Book();
        book.setTitle(title);
        List<Genre> genres = entityManager.getEntityManager().
                createQuery("SELECT g FROM Genre g WHERE g.name = :genreName", Genre.class).
                setParameter("genreName", genreName).getResultList();
        book.setGenre(genres.isEmpty() ? entityManager.persistAndFlush(new Genre(genreName)) : genres.get(0));
        for (String authorFullName : authorNames) {
            List<Author> authors = entityManager.getEntityManager().
                    createQuery("SELECT a FROM Author a WHERE a.fullName = :authorName", Author.class).
                    setParameter("authorName", authorFullName).getResultList();
            book.getAuthors().add(authors.isEmpty() ? entityManager.persistAndFlush(new Author(authorFullName)) :
                    authors.get(0));
        }
        entityManager.persist(book);
        entityManager.flush();
        entityManager.detach(book);
        return book;
    }
}