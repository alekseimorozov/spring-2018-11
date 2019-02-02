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

import java.util.ArrayList;
import java.util.List;
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
        String id = "id";
        Optional<Genre> optionalGenre = Optional.of(Genre.getGenre(id, "Test genre"));
        Optional<Author> optionalAuthor = Optional.of(Author.getAuthor(id, "Test Author"));
        Book testBook = new Book();
        testBook.setTitle("Test Book");
        testBook.setGenre(optionalGenre.get());
        testBook.getAuthors().add(optionalAuthor.get());
        InOrder inOrder = inOrder(genreDao, authorDao, bookDao);
        when(genreDao.findById(id)).thenReturn(optionalGenre);
        when(authorDao.findById(id)).thenReturn(optionalAuthor);
        when(bookDao.save(testBook)).thenReturn(testBook);

        Book actual = bookService.create("Test Book", id, id);
        inOrder.verify(genreDao).findById(id);
        inOrder.verify(authorDao).findById(id);
        inOrder.verify(bookDao).save(testBook);
        assertThat(actual).isEqualTo(testBook);
    }

    @Test
    @DisplayName("throws BiblioServiceException when try to create Book with not existing Genre")
    public void createBookWithGenreThatNotExistsTest() {
        assertThatExceptionOfType(BiblioServiceException.class)
                .isThrownBy(() -> bookService.create("title", "id", "id"))
                .withMessage("Genre with id: %s not exists", "id");
    }

    @Test
    @DisplayName("throws BiblioServiceException when try to create Book with not existing Author")
    public void createBookWithAuthorThatNotExistsTest() {
        when(genreDao.findById("id")).thenReturn(Optional.of(new Genre()));
        assertThatExceptionOfType(BiblioServiceException.class)
                .isThrownBy(() -> bookService.create("title", "id", "id"))
                .withMessage("Author with id: %s not exists", "id");
    }

    @Test
    @DisplayName("calls bookDao.findAll() to get all books")
    void readAll() {
        bookService.readAll();
        verify(bookDao).findAll();
    }

    @Test
    @DisplayName("calls bookDao.findById() to get required book")
    void readById() {
        when(bookDao.findById("id")).thenReturn(Optional.of(new Book()));
        bookService.readById("id");
        verify(bookDao).findById("id");
    }

    @Test
    @DisplayName("throws BiblioServiceException when try to get not existing Book")
    public void readByIdNotExistsTest() {
        String id = "testid";
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> bookService.readById(id))
                .withMessage("Book with id: %s not exists", id);
    }

    @Test
    @DisplayName("calls bookDao.findByTitle()")
    void findByName() {
        String testBookName = "test";
        bookService.findByName(testBookName);
        verify(bookDao).findByTitle(testBookName);
    }

    @Test
    @DisplayName("gets list of Authors with required name by authorDao.findByFullNameIgnoreCase() " +
    "then get List of Books with required Author by calling bookDao.findByAuthorsIsIn() ")
    void findBookByAuthorName() {
        String testAuthorName = "test";
        List<Author> authors = new ArrayList<>();
        InOrder inOrder = inOrder(authorDao, bookDao);
        when(authorDao.findByFullNameIgnoreCase(testAuthorName)).thenReturn(authors);

        bookService.findByAuthorName(testAuthorName);
        verify(bookDao).findByAuthorsIsIn(authors);
    }

    @Test
    @DisplayName("check if Author with required id exists by calling authorDao.findById()" +
            "and then calls bookDao.findByAuthors()")
    void findByAuthor() {
        Optional<Author> optionalAuthor = Optional.of(new Author());
        InOrder inOrder = inOrder(authorDao, bookDao);
        when(authorDao.findById("id")).thenReturn(optionalAuthor);
        bookService.findByAuthorId("id");
        inOrder.verify(authorDao).findById("id");
        inOrder.verify(bookDao).findByAuthors(optionalAuthor.get());
    }

    @Test
    @DisplayName("check if Genre exists by calling genreDao.findById() and then calls bookDao.findByGenre")
    void findByGenreId() {
        String id = "testId";
        Optional<Genre> optionalGenre = Optional.of(new Genre());
        InOrder inOrder = inOrder(genreDao, bookDao);
        when(genreDao.findById(id)).thenReturn(optionalGenre);

        bookService.findByGenreId(id);
        inOrder.verify(genreDao).findById(id);
        verify(bookDao).findByGenre(optionalGenre.get());
    }

    @Test
    @DisplayName("calls bookDao.findById, get and returns authors priperty of this book")
    public void findAuthorByBookTest() {
        Author firstAuthor = new Author("Author First");
        Author secondAuthor = new Author("Author Second");
        Book book = new Book();
        book.getAuthors().add(firstAuthor);
        book.getAuthors().add(secondAuthor);
        String id = "testId";
        when(bookDao.findById(id)).thenReturn(Optional.of(book));

        assertThat(bookService.findAuthorByBook(id)).containsOnly(firstAuthor, secondAuthor);
        verify(bookDao).findById(id);
    }

    @Test
    void updateBookName() {
        String id = "testId";
        String testBookName = "test";
        Book updatedBook = new Book();
        updatedBook.setId(id);
        InOrder inOrder = inOrder(bookDao);
        when(bookDao.findById(id)).thenReturn(Optional.of(updatedBook));

        bookService.updateBookName(id, testBookName);
        inOrder.verify(bookDao).findById(id);
        updatedBook.setTitle(testBookName);
        inOrder.verify(bookDao).save(updatedBook);
    }

    @Test
    @DisplayName("get updated Book by id, get required Genre by id, update and save Book")
    void updateBookGenre() {
        String id = "testId";
        Book updatedBook = new Book();
        updatedBook.setId(id);
        when(bookDao.findById(id)).thenReturn(Optional.of(updatedBook));
        Optional<Genre> optionalGenre = Optional.of(Genre.getGenre(id, "Updated Genre"));
        when(genreDao.findById(id)).thenReturn(optionalGenre);
        InOrder inOrder = inOrder(bookDao, genreDao, bookDao);

        bookService.updateBookGenre(id, id);
        inOrder.verify(bookDao).findById(id);
        inOrder.verify(genreDao).findById(id);
        updatedBook.setGenre(optionalGenre.get());
        inOrder.verify(bookDao).save(updatedBook);
    }

    @Test
    @DisplayName("throws BiblioServiceException when try to update Book with not existing Genre")
    public void updateBookGenreButGenreNotExistsTest() {
        String id = "testId";
        when(bookDao.findById(id)).thenReturn(Optional.of(new Book()));
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> bookService.updateBookGenre(id, id))
                .withMessage("Genre with id: %s not exists", id);
    }

    @Test
    @DisplayName("check that required Book and Author exist, then update and save Book")
    void addAuthorToBook() {
        String id = "testId";
        Book updatedBook = new Book();
        updatedBook.setId(id);
        Author author = Author.getAuthor(id, "Test");
        when(bookDao.findById(id)).thenReturn(Optional.of(updatedBook));
        when(authorDao.findById(id)).thenReturn(Optional.of(author));
        InOrder inOrder = inOrder(bookDao, authorDao, bookDao);

        bookService.addAuthorToBook(id, id);
        inOrder.verify(bookDao).findById(id);
        inOrder.verify(authorDao).findById(id);
        updatedBook.getAuthors().add(author);
        inOrder.verify(bookDao).save(updatedBook);
    }

    @Test
    @DisplayName("throws BiblioServiceException when try to update Book with not existing Author")
    public void addAuthorToBookButAuthorNotExistsTest() {
        String id = "testId";
        when(bookDao.findById(id)).thenReturn(Optional.of(new Book()));
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> bookService.addAuthorToBook(id, id))
                .withMessage("Author with id: %s not exists", id);
    }

    @Test
    @DisplayName("check if Book exists then update and save Book authors list")
    void removeAuthorFromBook() {
        String id = "testId";
        Book book = new Book();
        book.setId(id);
        Author removedAuthor = Author.getAuthor(id, "Test");
        book.getAuthors().add(removedAuthor);
        when(bookDao.findById(id)).thenReturn(Optional.of(book));
        InOrder inOrder = inOrder(bookDao);

        bookService.removeAuthorFromBook(id, id);
        inOrder.verify(bookDao).findById(id);
        inOrder.verify(bookDao).save(book);
        assertThat(book.getAuthors()).doesNotContain(removedAuthor);
    }

    @Test
    @DisplayName("throws BiblioServiceException when try to update not existing Book")
    public void updateBookButBookNotExistsTest() {
        String id = "testId";
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> bookService.updateBookName(id, "test"))
                .withMessage("Book with id: %s not exists", id);
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> bookService.updateBookGenre(id, id))
                .withMessage("Book with id: %s not exists", id);
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> bookService.addAuthorToBook(id, id))
                .withMessage("Book with id: %s not exists", id);
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> bookService.removeAuthorFromBook(id, id))
                .withMessage("Book with id: %s not exists", id);
    }

    @Test
    @DisplayName("delete Book with required id")
    public void delete() {
        bookService.delete("id");
        verify(bookDao).deleteById("id");
    }

    @Test
    @DisplayName("get Book by calling bookDao.findById(), then delete required comment from books comments and save " +
            "the Book by callling bookDao.save() ")
    public void deleteCommentTest() {
        String id = "testId";
        Book book = new Book();
        String commentOne = "comment one";
        String commentTwo = "commente two";
        String commentThree = "commente three";
        book.getComments().add(commentOne);
        book.getComments().add(commentTwo);
        book.getComments().add(commentThree);
        Optional<Book> optionalBook = Optional.of(book);
        InOrder inOrder = inOrder(bookDao);
        when(bookDao.findById(id)).thenReturn(optionalBook);

        bookService.deleteComment(id, 1);
        inOrder.verify(bookDao).findById(id);
        inOrder.verify(bookDao).save(book);
        assertThat(book.getComments()).containsOnly(commentOne, commentThree);
    }

    @Test
    @DisplayName("throws BiblioServiceException whet try to del comment by not existing id")
    public void deleteCommentByNotExistingIdTest() {
        String id = "testId";
        int commentId = 1;
        Book book = new Book();
        String commentOne = "comment one";
        book.getComments().add(commentOne);
        Optional<Book> optionalBook = Optional.of(book);
        when(bookDao.findById(id)).thenReturn(optionalBook);

        assertThatExceptionOfType(BiblioServiceException.class)
                .isThrownBy(() -> bookService.deleteComment(id, commentId))
                .withMessage("Comment with id: %d does not exist", commentId );
    }

    @Test
    @DisplayName("check and get Book with required Id if exists by calling bookDao.findById(), add comment to " +
            "this Book and save the Book by calling bookDao.save()")
    public void addCommentTest() {
        String id = "testId";
        Book book = new Book();
        String comment = "test comment";
        Optional<Book> optionalBook = Optional.of(book);
        InOrder inOrder = inOrder(bookDao);
        when(bookDao.findById(id)).thenReturn(optionalBook);

        bookService.addComment(id, comment);
        inOrder.verify(bookDao).findById(id);
        inOrder.verify(bookDao).save(book);
        assertThat(book.getComments()).containsOnly(comment);
    }

    @Test
    @DisplayName("at first find Book by bookId and then get and return comments of this Book")
    public void readCommentsByBookIdTest() {
        String id = "testId";
        Book book = new Book();
        String commentOne = "comment one";
        String commentTwo = "comment two";
        String commentThree = "comment three";
        book.getComments().add(commentOne);
        book.getComments().add(commentTwo);
        book.getComments().add(commentThree);
        Optional<Book> optionalBook = Optional.of(book);
        when(bookDao.findById(id)).thenReturn(optionalBook);

        List<String> expected = bookService.readCommentsByBookId(id);
        verify(bookDao).findById(id);
        assertThat(expected).containsOnly(commentOne, commentTwo, commentThree);
    }


    @Test
    @DisplayName("gets Book by colling bookDao.findById(), then edits required comment and save the Book by colling " +
            "bookDao.save()")
    public void updateComment() {
        String id = "testId";
        Book book = new Book();
        String commentOne = "comment one";
        String commentTwo = "comment two";
        String commentThree = "comment three";
        String updatedComment = "updated comment";
        book.getComments().add(commentOne);
        book.getComments().add(commentTwo);
        book.getComments().add(commentThree);
        Optional<Book> optionalBook = Optional.of(book);
        InOrder inOrder = inOrder(bookDao);
        when(bookDao.findById(id)).thenReturn(optionalBook);

        bookService.updateComment(id, 2, updatedComment);
        inOrder.verify(bookDao).findById(id);
        inOrder.verify(bookDao).save(book);
        assertThat(book.getComments().get(2)).isEqualTo(updatedComment);
        assertThat(book.getComments()).containsOnly(commentOne, commentTwo, updatedComment);
    }
}