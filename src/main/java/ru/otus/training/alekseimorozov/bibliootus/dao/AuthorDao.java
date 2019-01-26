package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;

import java.util.List;

@Repository
public interface AuthorDao extends CrudRepository<Author, Long> {
    List<Author> findByFullNameContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT a FROM Book b JOIN b.authors a WHERE b.id = :bookId")
    List<Author> findByBookId(@Param("bookId") Long bookId);
}