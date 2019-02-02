package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.serviceexception.BiblioServiceException;
import ru.otus.training.alekseimorozov.bibliootus.dao.AuthorDao;
import ru.otus.training.alekseimorozov.bibliootus.dao.BookDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;
import static ru.otus.training.alekseimorozov.bibliootus.entity.Author.getAuthor;

@DisplayName("class AuthorServiceImpl")
public class AuthorServiceImplTest extends CommonServiceTest {
    @MockBean(name = "authorDao")
    private AuthorDao authorDao;
    @MockBean(name = "bookDao")
    private BookDao bookDao;
    @Autowired
    private AuthorService authorService;

    @Test
    @DisplayName("calls  authorDao.save method to save new Author")
    public void createTest() {
        String testName = "Author Name";
        Author expectedAuthor = new Author(testName);
        authorService.create(testName);
        verify(authorDao).save(expectedAuthor);
    }

    @Test
    @DisplayName("calls authorDao.findAll() method to get list of all Authors")
    public void readAllTest() {
        authorService.readAll();
        verify(authorDao).findAll();
    }

    @Test
    @DisplayName("calls author.findById() method to get Author with required id")
    public void findByIdTest() {
        when(authorDao.findById("id")).thenReturn(Optional.of(new Author()));
        authorService.findById("id");
        verify(authorDao).findById("id");
    }

    @Test
    @DisplayName("throws BiblioServiceException if Author with required id was not found")
    public void findByIdNotFoundTest() {
        assertThatExceptionOfType(BiblioServiceException.class)
                .isThrownBy(() -> authorService.findById("id"))
                .withMessage("Author with id %s do not found", "id");
    }

    @Test
    @DisplayName("calls authorDao.findByFullNameIgnoreCase to get list of Authors by part of full name")
    public void findByNameTest() {
        String testAuthorName = "Author Name";
        authorService.findByName(testAuthorName);
        verify(authorDao).findByFullNameIgnoreCase(testAuthorName);
    }


    @Test
    @DisplayName("calls authorDao.findById()," +
            "then change Author's fullName and save Author by calls authorDao.save()")
    public void updateTest() {
        String id = "testId";
        Author updatedAuthor = getAuthor(id, "Test Author");
        InOrder inOrder = inOrder(authorDao);
        when(authorDao.findById(id)).thenReturn(Optional.of(updatedAuthor));
        String updatedAuthorName = "UPDATED Test Author";

        authorService.update(id, updatedAuthorName);
        inOrder.verify(authorDao).findById(id);
        inOrder.verify(authorDao).save(updatedAuthor);
        assertThat(updatedAuthor.getFullName()).isEqualTo(updatedAuthorName);
    }

    @Test
    @DisplayName("throws BiblioServiceException when try to update Author with not existing id ")
    public void updateNotExistingAuthorTest() {
        assertThatExceptionOfType(BiblioServiceException.class)
                .isThrownBy(() -> authorService.update("id", "Test"))
                .withMessage("Author with id %s do not found", "id");
    }

    @Test
    @DisplayName("check if Author exist, then checks that Books with this Author not exists and then delete Author")
    public void deleteTest() {
        Author author = getAuthor("id", "Test Author");
        InOrder inOrder = inOrder(authorDao, bookDao, authorDao);
        when(authorDao.findById("id")).thenReturn(Optional.of(author));
        when(bookDao.countBookByAuthors(author)).thenReturn(0);

        authorService.delete("id");
        inOrder.verify(authorDao).findById("id");
        inOrder.verify(bookDao).countBookByAuthors(author);
        inOrder.verify(authorDao).deleteById("id");
    }

    @Test
    @DisplayName("throws BiblioServiceException if try to delete not existing Author")
    public void deleteAuthorNotExistsTest() {
        assertThatExceptionOfType(BiblioServiceException.class)
                .isThrownBy(() -> authorService.delete("id"))
                .withMessage("Author with id %s do not found", "id");
    }

    @Test
    @DisplayName("throws BiblioServiceException if try to delete Author thet exists in some Books")
    public void deleteAuthorExistsInBookTest() {
        Author author = getAuthor("id", "Test Author");
        when(authorDao.findById("id")).thenReturn(Optional.of(author));
        when(bookDao.countBookByAuthors(author)).thenReturn(1);
        assertThatExceptionOfType(BiblioServiceException.class)
                .isThrownBy(() -> authorService.delete("id"))
                .withMessage("AUTHOR WASN'T REMOVED DUE TO SOME BOOKS HAVE LINK TO THIS AUTHOR\n" +
                        "REMOVE THIS AUTHOR FROM THE BOOKS FIRST");
    }
}