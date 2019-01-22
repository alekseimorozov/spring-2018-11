package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

@Repository
public interface GenreDao extends CrudRepository<Genre, Long> {
}