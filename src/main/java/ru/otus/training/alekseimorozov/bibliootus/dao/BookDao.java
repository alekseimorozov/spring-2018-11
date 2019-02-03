package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.List;

@Repository
public interface BookDao extends MongoRepository<Book, String> {
    @Query(value = "{title: {$regex: ?0, $options: 'i'}}")
    List<Book> findByTitle(String title);

    List<Book> findByAuthors(Author author);

    List<Book> findByAuthorsIsIn(List<Author> authors);

    List<Book> findByGenre(Genre genre);

    int countBookByAuthors(Author author);

    int countBookByGenre(Genre genre);
}