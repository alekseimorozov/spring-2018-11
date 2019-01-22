package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;

import java.util.List;

@Repository
public interface BookDao extends CrudRepository<Book, Long> {
//    @Query("SELECT DISTINCT b FROM Book b WHERE UPPER(b.title) LIKE :name")
    List<Book> findByTitleContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT b FROM Book b JOIN b.authors a WHERE UPPER(a.fullName) LIKE UPPER(CONCAT('%',:name,'%'))")
    List<Book> findByAuthorName(@Param("name") String name);

    @Query("SELECT DISTINCT b FROM Book b JOIN b.authors a WHERE a.id = :authorId")
    List<Book> findByAuthorId(@Param("authorId") Long authorId);

    @Query("SELECT DISTINCT b FROM Book b JOIN b.genre g WHERE g.id = :genreId")
    List<Book> findByGenreId(@Param("genreId") Long genreId);
}