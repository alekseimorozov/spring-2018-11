package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.serviceexception.BiblioServiceException;
import ru.otus.training.alekseimorozov.bibliootus.dao.AuthorDao;
import ru.otus.training.alekseimorozov.bibliootus.dao.BookDao;
import ru.otus.training.alekseimorozov.bibliootus.dao.GenreDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@DisplayName("class BookServiceImpl")
class BookServiceImplTest extends CommonServiceTest {
    @MockBean
    private BookDao bookDao;
    @MockBean
    private AuthorDao authorDao;
    @MockBean
    private GenreDao genreDao;
    @Autowired
    private BookService bookService;

    @Test
    @DisplayName("calls in order genreDao.findById(), authorDao.findById(), bookDao.save")
    void create() {
        Optional<Genre> optionalGenre = Optional.of(Genre.getGenre(1L, "Test genre"));
        Optional<Author> optionalAuthor = Optional.of(Author.getAuthor(1L, "Test Author"));
        Book testBook = new Book();
        testBook.setTitle("Test Book");
        testBook.setGenre(optionalGenre.get());
        testBook.getAuthors().add(optionalAuthor.get());
        InOrder inOrder = inOrder(genreDao, authorDao, bookDao);
        when(genreDao.findById(1L)).thenReturn(optionalGenre);
        when(authorDao.findById(1L)).thenReturn(optionalAuthor);
        when(bookDao.save(testBook)).thenReturn(testBook);

        bookService.create("Test Book", 1L, 1L);
        inOrder.verify(genreDao).findById(1L);
        inOrder.verify(authorDao).findById(1L);
        inOrder.verify(bookDao).save(testBook);
    }

    @Test
    @DisplayName("throws BiblioServiceException when try to create Book with not existing Genre")
    public void createBookWithGenreThatNotExistsTest() {
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> bookService.create("title", 1L, 1L))
                .withMessage("Genre with id: %d not exists", 1L);
    }

    @Test
    @DisplayName("throws BiblioServiceException when try to create Book with not existing Author")
    public void createBookWithAuthorThatNotExistsTest() {
        when(genreDao.findById(1L)).thenReturn(Optional.of(new Genre()));
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> bookService.create("title", 1L, 1L))
                .withMessage("Author with id: %d not exists", 1L);
    }

    @Test
    void readAll() {
        bookService.readAll();
        verify(bookDao).findAll();
    }

    @Test
    void readById() {
        when(bookDao.findById(1L)).thenReturn(Optional.of(new Book()));
        bookService.readById(1L);
        verify(bookDao).findById(1L);
    }

    @Test
    @DisplayName("throws BiblioServiceException when try to get not existing Book")
    public void readByIdNotExistsTest() {
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> bookService.readById(1L))
                .withMessage("Book with id: %d not exists", 1L);
    }

    @Test
    void findByName() {
        String testBookName = "test";
        bookService.findByName(testBookName);
        verify(bookDao).findByTitleContainingIgnoreCase(testBookName);
    }

    @Test
    void findByAuthorName() {
        String testAuthorName = "test";
        bookService.findByAuthorName(testAuthorName);
        verify(bookDao).findByAuthorName(testAuthorName);
    }

    @Test
    void findByAuthorId() {
        bookService.findByAuthorId(1L);
        verify(bookDao).findByAuthorId(1L);
    }

    @Test
    void findByGenreId() {
        bookService.findByGenreId(1L);
        verify(bookDao).findByGenreId(1L);
    }

    @Test
    void updateBookName() {
        String testBookName = "test";
        Book updatedBook = new Book();
        updatedBook.setId(1L);
        InOrder inOrder = inOrder(bookDao);
        when(bookDao.findById(1L)).thenReturn(Optional.of(updatedBook));

        bookService.updateBookName(1L, testBookName);
        inOrder.verify(bookDao).findById(1L);
        updatedBook.setTitle(testBookName);
        inOrder.verify(bookDao).save(updatedBook);
    }

    @Test
    @DisplayName("get updated Book by id, get required Genre by id, update and save Book")
    void updateBookGenre() {
        Book updatedBook = new Book();
        updatedBook.setId(1L);
        when(bookDao.findById(1L)).thenReturn(Optional.of(updatedBook));
        Optional<Genre> optionalGenre = Optional.of(Genre.getGenre(1L, "Updated Genre"));
        when(genreDao.findById(1L)).thenReturn(optionalGenre);
        InOrder inOrder = inOrder(bookDao, genreDao, bookDao);

        bookService.updateBookGenre(1L, 1L);
        inOrder.verify(bookDao).findById(1L);
        inOrder.verify(genreDao).findById(1L);
        updatedBook.setGenre(optionalGenre.get());
        inOrder.verify(bookDao).save(updatedBook);
    }

    @Test
    @DisplayName("throws BiblioServiceException when try to update Book with not existing Genre")
    public void updateBookGenreButGenreNotExistsTest() {
        when(bookDao.findById(1L)).thenReturn(Optional.of(new Book()));
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> bookService.updateBookGenre(1L, 1L))
                .withMessage("Genre with id: %d not exists", 1L);
    }

    @Test
    @DisplayName("check that required Book and Author exist, then update and save Book")
    void addAuthorToBook() {
        Book updatedBook = new Book();
        updatedBook.setId(1L);
        Author author = Author.getAuthor(1L, "Test");
        when(bookDao.findById(1L)).thenReturn(Optional.of(updatedBook));
        when(authorDao.findById(1L)).thenReturn(Optional.of(author));
        InOrder inOrder = inOrder(bookDao, authorDao, bookDao);

        bookService.addAuthorToBook(1L, 1L);
        inOrder.verify(bookDao).findById(1L);
        inOrder.verify(authorDao).findById(1L);
        updatedBook.getAuthors().add(author);
        inOrder.verify(bookDao).save(updatedBook);
    }

    @Test
    @DisplayName("throws BiblioServiceException when try to update Book with not existing Author")
    public void addAuthorToBookButAuthorNotExistsTest() {
        when(bookDao.findById(1L)).thenReturn(Optional.of(new Book()));
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> bookService.addAuthorToBook(1L, 1L))
                .withMessage("Author with id: %d not exists", 1L);
    }

    @Test
    @DisplayName("check if Book exists then update and save Book authors list")
    void removeAuthorFromBook() {
        Book book = new Book();
        book.setId(1L);
        Author removedAuthor = Author.getAuthor(1L, "Test");
        book.getAuthors().add(removedAuthor);
        when(bookDao.findById(1L)).thenReturn(Optional.of(book));
        InOrder inOrder = inOrder(bookDao);

        bookService.removeAuthorFromBook(1L, 1L);
        inOrder.verify(bookDao).findById(1L);
        inOrder.verify(bookDao).save(book);
        assertThat(book.getAuthors()).doesNotContain(removedAuthor);
    }

    @Test
    @DisplayName("throws BiblioServiceException when try to update not existing Book")
    public void updateBookButBookNotExistsTest() {
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> bookService.updateBookName(1L, "test"))
                .withMessage("Book with id: %d not exists", 1L);
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> bookService.updateBookGenre(1L, 1L))
                .withMessage("Book with id: %d not exists", 1L);
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> bookService.addAuthorToBook(1L, 1L))
                .withMessage("Book with id: %d not exists", 1L);
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> bookService.removeAuthorFromBook(1L, 1L))
                .withMessage("Book with id: %d not exists", 1L);
    }

    @Test
    @DisplayName("delete Book with required id")
    public void delete() {
        bookService.delete(1L);
        verify(bookDao).deleteById(1L);
    }
}