package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;

import java.util.List;

@Repository
public interface AuthorDao extends MongoRepository<Author, String> {
    @Query(value = "{fullName: {$regex: ?0, $options: 'i'}}")
    List<Author> findByFullNameIgnoreCase(String name);
}