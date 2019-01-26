package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.serviceexception.BiblioServiceException;
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
        bookCommentDao.save(bookComment);
    }

    @Override
    public List<BookComment> readAll() {
        return (List<BookComment>) bookCommentDao.findAll();
    }

    @Override
    public BookComment readById(Long id) {
        return checkAndReturnBookCommentIfExist(id);
    }

    @Override
    public List<BookComment> readByBookId(Long id) {
        return bookCommentDao.readByBookId(id);
    }

    @Override
    public void updateText(Long id, String text) {
        BookComment bookComment = checkAndReturnBookCommentIfExist(id);
        bookComment.setComment(text);
        bookCommentDao.save(bookComment);
    }

    @Override
    public void updateBook(Long commentId, Long bookId) throws IllegalStateException {
        BookComment bookComment = checkAndReturnBookCommentIfExist(commentId);
        bookComment.setBook(checkAndReturnBookIfExist(bookId));
        bookCommentDao.save(bookComment);
    }

    @Override
    public void delete(Long id) {
        try {
            bookCommentDao.deleteById(id);
        } catch(EmptyResultDataAccessException e) {
            throw new BiblioServiceException(String.format("BookComment with id %d do not found", id), e);
        }
    }

    private Book checkAndReturnBookIfExist(Long bookId) throws IllegalStateException {
        return bookDao.findById(bookId)
                .orElseThrow(() -> new BiblioServiceException(String.format("Book with id %d wasn't found", bookId)));
    }

    private BookComment checkAndReturnBookCommentIfExist(Long commentId) throws IllegalStateException {
        return bookCommentDao.findById(commentId)
                .orElseThrow(() -> new BiblioServiceException(String.format("BookComment with id: %d doesn't exist", commentId)));
    }
}