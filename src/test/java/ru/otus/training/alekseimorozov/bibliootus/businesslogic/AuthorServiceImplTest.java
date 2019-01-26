package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.serviceexception.BiblioServiceException;
import ru.otus.training.alekseimorozov.bibliootus.dao.AuthorDao;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@DisplayName("class AuthorServiceImpl")
public class AuthorServiceImplTest extends CommonServiceTest {
    @MockBean(name = "authorDao")
    private AuthorDao authorDao;
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
        when(authorDao.findById(1L)).thenReturn(Optional.of(new Author()));
        authorService.findById(1L);
        verify(authorDao).findById(1L);
    }

    @Test
    @DisplayName("throws BiblioServiceException if Author with required id was not found")
    public void findByIdNotFoundTest() {
        assertThatExceptionOfType(BiblioServiceException.class)
                .isThrownBy(() -> authorService.findById(1L))
                .withMessage("Author with id %d do not found", 1L);
    }

    @Test
    @DisplayName("calls authorDao.findByFullNameContainingIgnoreCase to get list of Authors by part of full name")
    public void findByNameTest() {
        String testAuthorName = "Author Name";
        authorService.findByName(testAuthorName);
        verify(authorDao).findByFullNameContainingIgnoreCase(testAuthorName);
    }

    @Test
    @DisplayName("calls authorDao.findByBookId() method to get list of Authors by Book id")
    public void findByBookTest() {
        authorService.findByBook(1L);
        verify(authorDao).findByBookId(1L);
    }

    @Test
    @DisplayName("calls authorDao.findById(), change Author's fullName and calls authorDao.save()")
    public void updateTest() {
        InOrder inOrder = inOrder(authorDao);
        Optional<Author> optionalAuthor = Optional.of(Author.getAuthor(1L, "Test Author"));
        when(authorDao.findById(1L)).thenReturn(optionalAuthor);
        String updatedAuthorName = "Updated Author Name";

        authorService.update(1L, updatedAuthorName);
        inOrder.verify(authorDao).findById(1L);
        Author updatedAuthor = optionalAuthor.get();
        updatedAuthor.setFullName(updatedAuthorName);
        inOrder.verify(authorDao).save(updatedAuthor);
    }

    @Test
    @DisplayName("throws BiblioServiceException when try to update Author with not existing id ")
    public void updateNotExistingAuthorTest() {
        assertThatExceptionOfType(BiblioServiceException.class)
                .isThrownBy(() -> authorService.update(1L, "Test"))
                .withMessage("Author with id %d do not found", 1L);
    }

    @Test
    @DisplayName("calls authorDao.deleteById()")
    public void deleteTest() {
        authorService.delete(1L);
        verify(authorDao).deleteById(1L);
    }

    @Test
    @DisplayName("throws BiblioServiceException if try to delete not existing Author")
    public void deleteAuthorNotExistsTest() {
//        authorService.delete(11L);
        assertThatExceptionOfType(BiblioServiceException.class).isThrownBy(() -> authorService.delete(77L));
    }
}