package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.training.alekseimorozov.bibliootus.dao.BookCommentDao;
import ru.otus.training.alekseimorozov.bibliootus.dao.BookDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.BookComment;

import static org.mockito.Mockito.*;

@DisplayName("class BookServiceImpl")
class BookCommentServiceImplServiceTest extends CommonServiceTest {
//    @MockBean(name = "bookDao")
//    private BookDao bookDao;
//    @MockBean(name = "bookCommentDao")
//    private BookCommentDao bookCommentDao;
//
//    @Autowired
//    private BookCommentService bookCommentService;
//
//    @Test
//    @DisplayName("saves new BookComment into database")
//    void addComment() {
//        Long testBookId = 1L;
//        Book book = new Book();
//        book.setId(testBookId);
//        String testCommentText = "Test BookComment text";
//        BookComment testBookComment = new BookComment(testCommentText);
//        testBookComment.setBook(book);
//        when(bookDao.readById(1L)).thenReturn(book);
//        InOrder inOrder = inOrder(bookDao, bookCommentDao);
//
//        bookCommentService.addComment(testBookId, testCommentText);
//        inOrder.verify(bookDao).readById(testBookId);
//        inOrder.verify(bookCommentDao).create(testBookComment);
//    }
//
//    @Test
//    @DisplayName("returns list of all BookComments")
//    void readAll() {
//        bookCommentService.readAll();
//        verify(bookCommentDao).readAll();
//    }
//
//    @Test
//    @DisplayName("returns BookComment with required id")
//    void readById() {
//        bookCommentService.readById(1L);
//        verify(bookCommentDao).readById(1L);
//    }
//
//    @Test
//    @DisplayName("returns list of BookComments for Book with required id")
//    void readByBookId() {
//        bookCommentService.readByBookId(1L);
//        verify(bookCommentDao).readByBookId(1L);
//    }
//
//    @Test
//    @DisplayName("updates text of BookComments with requiared id")
//    void updateText() {
//        Long testCommentId = 1L;
//        String updatedText = "Updated comment";
//        BookComment comment = new BookComment("Old comment");
//        comment.setId(testCommentId);
//        when(bookCommentDao.readById(testCommentId)).thenReturn(comment);
//        InOrder inOrder = inOrder(bookCommentDao);
//
//        bookCommentService.updateText(testCommentId, updatedText);
//        inOrder.verify(bookCommentDao).readById(testCommentId);
//        comment.setComment("Test");
//        inOrder.verify(bookCommentDao).update(comment);
//    }
//
//    @Test
//    @DisplayName("updates Book for BookComment with required id")
//    void updateBook() {
//        Book oldBook = new Book();
//        oldBook.setId(1L);
//        oldBook.setTitle("OldBook");
//        Long updatedBookId = 2L;
//        Book updatedBook = new Book();
//        updatedBook.setId(updatedBookId);
//        updatedBook.setTitle("UpdatedBook");
//        Long updatedCommentId = 1L;
//        BookComment updatedComment = new BookComment("Updated comment");
//        updatedComment.setId(updatedCommentId);
//        updatedComment.setBook(oldBook);
//        when(bookCommentDao.readById(updatedCommentId)).thenReturn(updatedComment);
//        when(bookDao.readById(updatedBookId)).thenReturn(updatedBook);
//        InOrder inOrder = inOrder(bookCommentDao, bookDao, bookCommentDao);
//
//        bookCommentService.updateBook(updatedCommentId, updatedBookId);
//        inOrder.verify(bookCommentDao).readById(updatedCommentId);
//        updatedComment.setBook(inOrder.verify(bookDao).readById(updatedBookId));
//        inOrder.verify(bookCommentDao).update(updatedComment);
//    }
//
//    @Test
//    void delete() {
//        bookCommentService.delete(1L);
//        verify(bookCommentDao).delete(1L);
//    }
}