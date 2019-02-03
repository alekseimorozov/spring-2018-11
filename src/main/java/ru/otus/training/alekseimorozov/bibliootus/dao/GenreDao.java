package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

@Repository
public interface GenreDao extends MongoRepository<Genre, String> {
}