package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.training.alekseimorozov.bibliootus.entity.BookComment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class BookCommentDaoOrm implements BookCommentDao {
    private static final String READ_ALL = "SELECT bc FROM BookComment bc ";
    private static final String READ_BY_BOOK_ID = READ_ALL + "WHERE bc.book.id = :id";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public BookComment create(BookComment comment) {
        entityManager.persist(comment);
        entityManager.flush();
        return comment;
    }

    @Override
    public BookComment readById(Long id) {
        BookComment comment = entityManager.find(BookComment.class, id);
        return comment == null ? new BookComment() : comment;
    }

    @Override
    public List<BookComment> readAll() {
        return entityManager.createQuery(READ_ALL, BookComment.class).getResultList();
    }

    @Override
    public List<BookComment> readByBookId(Long id) {
        return entityManager.createQuery(READ_BY_BOOK_ID, BookComment.class).setParameter("id", id).getResultList();
    }

    @Transactional
    @Override
    public void update(BookComment comment) {
        entityManager.merge(comment);
        entityManager.flush();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        BookComment comment = entityManager.find(BookComment.class, id);
        if (comment != null) {
            entityManager.remove(comment);
            entityManager.flush();
        }
    }
}