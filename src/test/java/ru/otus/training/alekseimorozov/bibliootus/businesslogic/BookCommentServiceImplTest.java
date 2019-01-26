package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.serviceexception.BiblioServiceException;
import ru.otus.training.alekseimorozov.bibliootus.dao.BookCommentDao;
import ru.otus.training.alekseimorozov.bibliootus.dao.BookDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.BookComment;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@DisplayName("class BookServiceImpl")
class BookCommentServiceImplTest extends CommonServiceTest {
    @MockBean(name = "bookDao")
    private BookDao bookDao;
    @MockBean(name = "bookCommentDao")
    private BookCommentDao bookCommentDao;

    @Autowired
    private BookCommentService bookCommentService;

    @Test
    @DisplayName("saves new BookComment into database")
    void addCommentTest() {
        Long testBookId = 1L;
        Book book = new Book();
        book.setId(testBookId);
        String testCommentText = "Test BookComment text";
        BookComment testBookComment = new BookComment(testCommentText);
        testBookComment.setBook(book);
        Optional<Book> optionalBook = Optional.of(book);
        InOrder inOrder = inOrder(bookDao, bookCommentDao);
        when(bookDao.findById(1L)).thenReturn(optionalBook);

        bookCommentService.addComment(testBookId, testCommentText);
        inOrder.verify(bookDao).findById(testBookId);
        inOrder.verify(bookCommentDao).save(testBookComment);
    }

    @Test
    @DisplayName("Throws BiblioServiceException when try add comment to not existing Book")
    public void createCommentForNotExistingBookTest() {
        assertThatExceptionOfType(BiblioServiceException.class)
                .isThrownBy(() -> bookCommentService.addComment(1L, "Test"))
                .withMessage("Book with id %d wasn't found", 1L);
    }

    @Test
    @DisplayName("calls bookDao.findAll() when need to returns list of all BookComments")
    void readAllTest() {
        bookCommentService.readAll();
        verify(bookCommentDao).findAll();
    }

    @Test
    @DisplayName("returns BookComment with required id")
    void readByIdTest() {
        Optional<BookComment> optionalComment = Optional.of(new BookComment());
        when(bookCommentDao.findById(1L)).thenReturn(optionalComment);

        bookCommentService.readById(1L);
        verify(bookCommentDao).findById(1L);
    }

    @Test
    @DisplayName("Throws BiblioServiceException if BookComment with required id does not exist")
    void readByIdNotExistTest() {
        assertThatExceptionOfType(BiblioServiceException.class)
                .isThrownBy(() -> bookCommentService.readById(1L))
                .withMessage("BookComment with id: %d doesn't exist", 1L);
    }

    @Test
    @DisplayName("returns list of BookComments for Book with required id")
    void readByBookId() {
        bookCommentService.readByBookId(1L);
        verify(bookCommentDao).readByBookId(1L);
    }

    @Test
    @DisplayName("updates text of BookComments with requiared id")
    void updateText() {
        Long testCommentId = 1L;
        String updatedText = "Updated comment";
        BookComment comment = new BookComment("Old comment");
        comment.setId(testCommentId);
        Optional<BookComment> optionalComment = Optional.of(comment);
        when(bookCommentDao.findById(testCommentId)).thenReturn(optionalComment);
        InOrder inOrder = inOrder(bookCommentDao);

        bookCommentService.updateText(testCommentId, updatedText);
        inOrder.verify(bookCommentDao).findById(testCommentId);
        comment.setComment("Test");
        inOrder.verify(bookCommentDao).save(comment);
    }


    @Test
    @DisplayName("Throws BiblioServiceException if updated BookComment with required id does not exist")
    void updateTextCommentNotExistTest() {
        assertThatExceptionOfType(BiblioServiceException.class)
                .isThrownBy(() -> bookCommentService.updateText(1L, "Test"))
                .withMessage("BookComment with id: %d doesn't exist", 1L);
    }

    @Test
    @DisplayName("updates Book for BookComment with required id")
    void updateBook() {
        Long updatedCommentId = 1L;
        BookComment updatedComment = new BookComment("Updated comment");
        updatedComment.setId(updatedCommentId);
        updatedComment.setBook(new Book());
        Optional<BookComment> optionalComment = Optional.of(updatedComment);
        Long updatedBookId = 2L;
        Book updatedBook = new Book();
        updatedBook.setId(updatedBookId);
        updatedBook.setTitle("UpdatedBook");
        Optional<Book> optionalBook = Optional.of(updatedBook);
        when(bookCommentDao.findById(updatedCommentId)).thenReturn(optionalComment);
        when(bookDao.findById(updatedBookId)).thenReturn(optionalBook);
        InOrder inOrder = inOrder(bookCommentDao, bookDao, bookCommentDao);

        bookCommentService.updateBook(updatedCommentId, updatedBookId);
        inOrder.verify(bookCommentDao).findById(updatedCommentId);
        inOrder.verify(bookDao).findById(updatedBookId);
        updatedComment.setBook(updatedBook);
        inOrder.verify(bookCommentDao).save(updatedComment);
    }

    @Test
    @DisplayName("Throws BiblioServiceException if updated BookComment with required id does not exist")
    void updateBookCommentNotExistTest() {
        assertThatExceptionOfType(BiblioServiceException.class)
                .isThrownBy(() -> bookCommentService.updateBook(1L, 1L))
                .withMessage("BookComment with id: %d doesn't exist", 1L);
    }

    @Test
    @DisplayName("Throws BiblioServiceException when try update comment with not existing Book")
    public void updateBookForNotExistingBookTest() {
        when(bookCommentDao.findById(1L)).thenReturn(Optional.of(new BookComment()));
        assertThatExceptionOfType(BiblioServiceException.class)
                .isThrownBy(() -> bookCommentService.updateBook(1L, 1L))
                .withMessage("Book with id %d wasn't found", 1L);
    }

    @Test
    @DisplayName("calls bookCommentDao.deleteById() method")
    void delete() {
        bookCommentService.delete(1L);
        verify(bookCommentDao).deleteById(1L);
    }
}