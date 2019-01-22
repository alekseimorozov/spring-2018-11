package ru.otus.training.alekseimorozov.bibliootus.dao;

import ru.otus.training.alekseimorozov.bibliootus.entity.BookComment;

import java.util.List;

public interface BookCommentDao {
    BookComment create(BookComment comment);

    BookComment readById(Long id);

    List<BookComment> readAll();

    List<BookComment> readByBookId(Long id);

    void update(BookComment comment);

    void delete(Long id);
}