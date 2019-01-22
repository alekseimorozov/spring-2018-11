package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import ru.otus.training.alekseimorozov.bibliootus.entity.BookComment;

import java.util.List;

public interface BookCommentService {
    void addComment(Long bookId, String comment);

    List<BookComment> readAll();

    BookComment readById(Long id);

    List<BookComment> readByBookId(Long id);

    void updateText(Long id, String text);

    void updateBook(Long commentId, Long bookId);

    void delete(Long id);
}
