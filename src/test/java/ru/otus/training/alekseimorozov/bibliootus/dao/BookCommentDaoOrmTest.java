package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.BookComment;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("class BookCommentDaoOrm")
class BookCommentDaoOrmTest extends CommonDaoOrmTest {
    @Autowired
    private BookCommentDao bookCommentDao;

    @Test
    @DisplayName("saves new comment into database")
    void create() {
        Book book = createBook("Test");
        BookComment expected = new BookComment("Test comment");
        expected.setBook(book);
        bookCommentDao.create(expected);
        assertThat(getEntityManager().find(BookComment.class, expected.getId())).isEqualTo(expected);
    }

    @Test
    @DisplayName("throws  IllegalStateException wheh try to save new comment into database with empty Book")
    void createWithEmptyBook() {
        Book emptyBook = new Book();
        BookComment expected = new BookComment("Test comment");
        expected.setBook(emptyBook);
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> bookCommentDao.create(expected));
    }

    @Test
    @DisplayName("reads BookComment with required id")
    void readById() {
        BookComment expected = createBookComment("Test comment", createBook("Test book"));
        assertThat(bookCommentDao.readById(expected.getId())).isEqualTo(expected);
    }

    @Test
    @DisplayName("returns list of all BookComments")
    void readAll() {
        Book bookOne = createBook("Book one");
        Book bookTwo = createBook("Book two");
        Book bookThree = createBook("Book three");
        BookComment first = createBookComment("First comment", bookOne);
        BookComment second = createBookComment("Second comment", bookTwo);
        BookComment third = createBookComment("Third comment", bookOne);
        BookComment fourth = createBookComment("Fourth comment", bookThree);
        assertThat(bookCommentDao.readAll()).containsOnly(first, second, third, fourth);
    }

    @Test
    @DisplayName("returns list of all BookComments for book with required id")
    void readByBookId() {
        Book bookOne = createBook("Book one");
        Book bookTwo = createBook("Book two");
        Book bookThree = createBook("Book three");
        BookComment first = createBookComment("First comment", bookOne);
        BookComment second = createBookComment("Second comment", bookTwo);
        BookComment third = createBookComment("Third comment", bookOne);
        BookComment fourth = createBookComment("Fourth comment", bookThree);
        assertThat(bookCommentDao.readByBookId(bookOne.getId())).containsOnly(first, third);
    }

    @Test
    @DisplayName("returns empty ArrayList<BookComment> if book with required id dosn't has comments")
    void readByBookIdNoComments() {
        Book bookOne = createBook("Book one");
        Book bookTwo = createBook("Book two");
        Book bookThree = createBook("Book three");
        BookComment first = createBookComment("First comment", bookOne);
        BookComment second = createBookComment("Second comment", bookTwo);
        BookComment third = createBookComment("Third comment", bookOne);
        BookComment fourth = createBookComment("Fourth comment", bookTwo);
        assertThat(bookCommentDao.readByBookId(bookThree.getId())).isEqualTo(new ArrayList<BookComment>());
    }

    @Test
    @DisplayName("updates BookComment text")
    void updateBookCommentText() {
        BookComment expected = createBookComment("Test comment", createBook("Test"));
        expected.setComment("Edited comment");
        bookCommentDao.update(expected);
        assertThat(getEntityManager().find(BookComment.class, expected.getId())).isEqualTo(expected);
    }

    @Test
    @DisplayName("update book of BookComment")
    void updateBookCommentBook() {
        Book one = createBook("One");
        Book two = createBook("Two");
        BookComment expected = createBookComment("Test comment", one);
        expected.setBook(two);
        bookCommentDao.update(expected);
        assertThat(getEntityManager().find(BookComment.class, expected.getId())).isEqualTo(expected);
    }

    @Test
    @DisplayName("update book of BookComment")
    void updateBookCommentBookWithEmptyBook() {
        Book one = createBook("One");
        BookComment expected = createBookComment("Test comment", one);
        expected.setBook(new Book());
        bookCommentDao.update(expected);
//        assertThat(getEntityManager().find(BookComment.class, expected.getId())).isEqualTo(expected);
    }

    @Test
    @DisplayName("deletes BookComment with required id")
    void delete() {
        BookComment expected = createBookComment("Test comment", createBook("Test"));
        bookCommentDao.delete(expected.getId());
        assertThat(getEntityManager().find(BookComment.class, expected.getId())).isNull();
    }

    private BookComment createBookComment(String text, Book book) {
        List<BookComment> comments = getEntityManager().getEntityManager().
                createQuery("SELECT bc FROM BookComment bc WHERE bc.comment = :comment", BookComment.class).
                setParameter("comment", text).getResultList();
        return comments.isEmpty() ? persistAndFlushComment(text, book) : comments.get(0);
    }

    private BookComment persistAndFlushComment(String text, Book book) {
        BookComment comment = new BookComment(text);
        comment.setBook(book);
        getEntityManager().persistAndFlush(comment);
        getEntityManager().detach(comment);
        return comment;
    }

    private Book createBook(String title) {
        List<Book> books = getEntityManager().getEntityManager().
                createQuery("SELECT b FROM Book b WHERE b.title = :title", Book.class).
                setParameter("title", title).getResultList();
        return books.isEmpty() ? persistAndFlushBook(title) : books.get(0);
    }

    private Book persistAndFlushBook(String title) {
        Book book = new Book();
        book.setTitle(title);
        getEntityManager().persistAndFlush(book);
        getEntityManager().detach(book);
        return book;
    }
}