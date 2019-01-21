package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.training.alekseimorozov.bibliootus.dao.AuthorDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class AuthorServiceImplServiceTest extends CommonServiceTest {
    @MockBean
    private AuthorDao authorDao;
    @Autowired
    private AuthorService authorService;

    @Test
    public void createTest() {
        String testName = "Author Name";
        Author expectedAuthor = new Author();
        expectedAuthor.setFullName(testName);
        Author actualAuthor = authorService.create(testName);
        verify(authorDao).create(expectedAuthor);
        assertEquals(expectedAuthor, actualAuthor);
    }

    @Test
    public void readAllTest() {
        authorService.readAll();
        verify(authorDao).readAll();
    }

    @Test
    public void findByIdTest() {
        authorService.findById(1L);
        verify(authorDao).findById(1L);
    }

    @Test
    public void findByNameTest() {
        String testAuthorName = "Author Name";
        authorService.findByName(testAuthorName);
        verify(authorDao).findByName(testAuthorName);
    }

    @Test
    public void findByBookTest() {
        authorService.findByBook(1L);
        verify(authorDao).findByBookId(1L);
    }

    @Test
    public void updateTest() {
        String testAuthorName = "Author Test";
        authorService.update(1L, testAuthorName);
        verify(authorDao).update(1L, testAuthorName);
    }

    @Test
    public void deleteTest() {
        authorService.delete(1L);
        verify(authorDao).delete(1L);
    }
}