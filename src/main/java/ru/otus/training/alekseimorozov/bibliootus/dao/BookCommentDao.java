package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.training.alekseimorozov.bibliootus.entity.BookComment;

import java.util.List;

@Repository
public interface BookCommentDao extends CrudRepository<BookComment, Long> {
    @Query("SELECT bc FROM BookComment bc WHERE bc.book.id = :bookId")
    List<BookComment> readByBookId(Long bookId);
}