package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.training.alekseimorozov.bibliootus.entity.BookComment;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DisplayName("repository BookCommentDao")
class BookCommentDaoTest extends CommonDaoJpaTest {
    @Autowired
    private BookCommentDao bookCommentDao;

    @Test
    @DisplayName("find BookComments for Book with required id")
    public void readByBookId() {
        BookComment first = getEntityManager().find(BookComment.class, 1L);
        BookComment second = getEntityManager().find(BookComment.class, 2L);
        assertThat(bookCommentDao.readByBookId(1L)).containsOnly(first, second);
    }

    @Test
    @DisplayName("returns empty list BookComments if Book with required id not found")
    public void readByBookIdNotFound() {
        assertThat(bookCommentDao.readByBookId(-1L)).hasSize(0);
    }
}