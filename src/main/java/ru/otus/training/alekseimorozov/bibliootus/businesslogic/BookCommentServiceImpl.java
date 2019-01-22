package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.training.alekseimorozov.bibliootus.dao.BookCommentDao;
import ru.otus.training.alekseimorozov.bibliootus.dao.BookDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.BookComment;

import java.util.List;

@Service
public class BookCommentServiceImpl implements BookCommentService {
    private BookDao bookDao;
    private BookCommentDao bookCommentDao;

    @Autowired
    public BookCommentServiceImpl(BookDao bookDao, BookCommentDao bookCommentDao) {
        this.bookDao = bookDao;
        this.bookCommentDao = bookCommentDao;
    }

    @Override
    public void addComment(Long bookId, String comment) throws IllegalStateException {
        BookComment bookComment = new BookComment(comment);
        bookComment.setBook(checkAndReturnBookIfExist(bookId));
        bookCommentDao.create(bookComment);
    }

    @Override
    public List<BookComment> readAll() {
        return bookCommentDao.readAll();
    }

    @Override
    public BookComment readById(Long id) {
        return bookCommentDao.readById(id);
    }

    @Override
    public List<BookComment> readByBookId(Long id) {
        return bookCommentDao.readByBookId(id);
    }

    @Override
    public void updateText(Long id, String text) {
        BookComment bookComment = checkAndReturnBookCommentIfExist(id);
        bookComment.setComment(text);
        bookCommentDao.update(bookComment);
    }

    @Override
    public void updateBook(Long commentId, Long bookId) throws IllegalStateException {
        BookComment bookComment = checkAndReturnBookCommentIfExist(commentId);
        bookComment.setBook(checkAndReturnBookIfExist(bookId));
        bookCommentDao.update(bookComment);
    }

    private Book checkAndReturnBookIfExist(Long bookId) throws IllegalStateException {
        Book book = bookDao.readById(bookId);
        if (book.getId() == null) {
            throw new IllegalStateException(String.format("Book with id: %d doesn't exist", bookId));
        }
        return book;
    }

    private BookComment checkAndReturnBookCommentIfExist(Long commentId) throws IllegalStateException {
        BookComment comment = bookCommentDao.readById(commentId);
        if (comment.getId() == null) {
            throw new IllegalStateException(String.format("BookComment with id: %d doesn't exist", commentId));
        }
        return comment;
    }

    @Override
    public void delete(Long id) {
        bookCommentDao.delete(id);
    }
}