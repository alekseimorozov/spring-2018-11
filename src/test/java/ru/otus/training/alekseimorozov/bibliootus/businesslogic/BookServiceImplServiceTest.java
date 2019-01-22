package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.training.alekseimorozov.bibliootus.dao.BookDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;

import static org.mockito.Mockito.verify;

class BookServiceImplServiceTest extends CommonServiceTest {
//    @MockBean
//    private BookDao bookDao;
//    @Autowired
//    private BookService bookService;
//
//    @Test
//    void create() {
//        Book testBook = new Book();
//        bookService.create(testBook);
//        verify(bookDao).save(testBook);
//    }
//
//    @Test
//    void readAll() {
//        bookService.readAll();
//        verify(bookDao).findAll();
//    }
//
//    @Test
//    void readById() {
//        bookService.readById(1L);
//        verify(bookDao).findById(1L);
//    }
//
//    @Test
//    void findByName() {
//        String testBookName = "test";
//        bookService.findByName(testBookName);
//        verify(bookDao).findByTitleContainingIgnoreCase(testBookName);
//    }
//
//    @Test
//    void findByAuthorName() {
//        String testAuthorName = "test";
//        bookService.findByAuthorName(testAuthorName);
//        verify(bookDao).findByAuthorName(testAuthorName);
//    }
//
//    @Test
//    void findByAuthorId() {
//        bookService.findByAuthorId(1L);
//        verify(bookDao).findByAuthorId(1L);
//    }
//
//    @Test
//    void findByGenreId() {
//        bookService.findByGenreId(1L);
//        verify(bookDao).findByGenreId(1L);
//
//    }
//
//    @Test
//    void updateBookName() {
//        String testBookName = "test";
//        bookService.updateBookName(1L, testBookName);
//        Book book = new Book();
//        book.setId(1L);
//        book.setTitle(testBookName);
//        verify(bookDao).save(book);
//    }
//
//    @Test
//    void updateBookGenre() {
//        bookService.updateBookGenre(1L, 1L);
//        Book book = new Book();
//        verify(bookDao).updateBookGenre(1L, 1L);
//    }
//
//    @Test
//    void addAuthorToBook() {
//        bookService.addAuthorToBook(1L, 1L);
//        verify(bookDao).addAuthorToBook(1L, 1L);
//    }
//
//    @Test
//    void removeAuthorFromBook() {
//        bookService.removeAuthorFromBook(1L, 1L);
//        verify(bookDao).removeAuthorFromBook(1L, 1L);
//    }
//
//    @Test
//    void delete() {
//        bookService.delete(1L);
//        verify(bookDao).delete(1L);
//    }
}