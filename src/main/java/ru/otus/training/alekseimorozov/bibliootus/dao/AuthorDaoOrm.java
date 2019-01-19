package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class AuthorDaoOrm implements AuthorDao {
    private static final String READ_ALL = "SELECT DISTINCT a FROM Author a ";
    private static final String FIND_BY_NAME = READ_ALL + "WHERE UPPER(a.fullName) LIKE :name";
    private static final String FIND_BY_BOOK = "SELECT DISTINCT a FROM Book b JOIN b.authors a WHERE b.id = :bookId";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void create(Author author) {
        entityManager.persist(author);
        entityManager.flush();
    }

    @Override
    public List<Author> readAll() {
        return entityManager.createQuery(READ_ALL, Author.class).getResultList();
    }

    @Override
    public Author findById(Long id) {
        Author author = entityManager.find(Author.class, id);
        return author == null ? new Author() : author;
    }

    @Override
    public List<Author> findByName(String name) {
        return entityManager.createQuery(FIND_BY_NAME, Author.class).
                setParameter("name", "%" + name.toUpperCase() + "%").getResultList();
    }

    @Override
    public List<Author> findByBookId(Long bookId) {
        return entityManager.createQuery(FIND_BY_BOOK, Author.class).setParameter("bookId", bookId).getResultList();
    }

    @Override
    public void update(Long id, String name) {

    }

    @Transactional
    @Override
    public void update(Author author) {
        entityManager.merge(author);
        entityManager.flush();
    }

    @Transactional
    @Override
    public void delete(Long authorId) {
        Author author = entityManager.find(Author.class, authorId);
        if (author != null) {
            entityManager.remove(author);
            entityManager.flush();
        }
    }
}