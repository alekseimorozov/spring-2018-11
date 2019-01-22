package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class GenreDaoOrm implements GenreDao {
    private static final String READ_ALL = "SELECT g FROM Genre g";
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Genre create(Genre genre) {
        entityManager.persist(genre);
        entityManager.flush();
        return genre;
    }

    @Override
    public List<Genre> readAll() {
        return entityManager.createQuery(READ_ALL, Genre.class).getResultList();
    }

    @Override
    public Genre readById(Long genreId) {
        Genre genre = entityManager.find(Genre.class, genreId);
        return genre == null ? new Genre() : genre;
    }

    @Override
    public void update(Long genreId, String name) {

    }

    @Transactional
    @Override
    public void update(Genre genre) {
        entityManager.merge(genre);
        entityManager.flush();
    }

    @Transactional
    @Override
    public void delete(Long genreId) {
        Genre genre = entityManager.find(Genre.class, genreId);
        if (genre != null) {
            entityManager.remove(genre);
            entityManager.flush();
        }
    }
}