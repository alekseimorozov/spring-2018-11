package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class BookDaoOrm implements BookDao {
    private static final String READ_ALL = "SELECT DISTINCT b FROM Book b ";
    private static final String READ_BY_NAME = READ_ALL + "WHERE UPPER(b.title) LIKE :name";
    private static final String READ_BY_AUTHOR_NAME = READ_ALL + "JOIN b.authors a WHERE UPPER(a.fullName) LIKE :name";
    private static final String READ_BY_AUTHOR_ID = READ_ALL + "JOIN b.authors a WHERE a.id = :id";
    private static final String READ_BY_GENRE_ID = READ_ALL + "JOIN b.genre g WHERE g.id = :id";
    private static final String DELETE_BOOK_COMMENT = "DELETE FROM BookComment bc WHERE bc.book.id = :bookId";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Book create(Book book) {
        entityManager.persist(book);
        entityManager.flush();
        return book;
    }

    @Override
    public List<Book> readAll() {
        return entityManager.createQuery(READ_ALL, Book.class).getResultList();
    }

    @Override
    public Book readById(Long id) {
        Book book = entityManager.find(Book.class, id);
        return book == null ? new Book() : book;
    }

    @Override
    public List<Book> findByName(String name) {
        return entityManager.createQuery(READ_BY_NAME, Book.class).
                setParameter("name", "%" + name.toUpperCase() + "%").getResultList();
    }

    @Override
    public List<Book> findByAuthorName(String name) {
        return entityManager.createQuery(READ_BY_AUTHOR_NAME, Book.class).
                setParameter("name", "%" + name.toUpperCase() + "%").getResultList();
    }

    @Override
    public List<Book> findByAuthorId(Long id) {
        return entityManager.createQuery(READ_BY_AUTHOR_ID, Book.class).setParameter("id", id).getResultList();
    }

    @Override
    public List<Book> findByGenreId(Long id) {
        return entityManager.createQuery(READ_BY_GENRE_ID, Book.class).setParameter("id", id).getResultList();
    }

    @Override
    public void updateBookName(Long id, String name) {

    }

    @Override
    public void updateBookGenre(Long bookId, Long genreId) {

    }

    @Override
    public void addAuthorToBook(Long bookId, Long authorId) {

    }

    @Override
    public void removeAuthorFromBook(Long bookId, Long authorId) {

    }

    @Transactional
    @Override
    public void update(Book book) {
        entityManager.merge(book);
        entityManager.flush();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Book book = entityManager.find(Book.class, id);
        if (book != null) {
            entityManager.createQuery(DELETE_BOOK_COMMENT).setParameter("bookId", id).executeUpdate();
            entityManager.remove(book);
            entityManager.flush();
        }
    }
}