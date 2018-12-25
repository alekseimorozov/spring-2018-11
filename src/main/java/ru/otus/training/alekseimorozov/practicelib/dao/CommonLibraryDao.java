package ru.otus.training.alekseimorozov.practicelib.dao;

import java.io.Serializable;
import java.util.List;

public interface CommonLibraryDao<T extends Serializable> {
    void create(T entity);
    T readById(Long id);
    List<T> readAll();
    int update(T entity);
    int delete(T entity);
}
