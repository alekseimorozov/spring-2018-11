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
            "then get all Books, cantainig this Author by colling bookDao.findByAuthors," +
            "then change Author's fullName and save Author by calls authorDao.save()" +
            "then change this Author in all Books authors list and save changes by calling bookDao.saveAll")
    public void updateTest() {
        String id = "testId";
        Author updatedAuthor = getAuthor(id, "Test Author");
        Author authorOne = getAuthor("one", "Other Author One");
        Author authorTwo = getAuthor("two", "Other Author Two");
        Book bookOne = new Book();
        bookOne.getAuthors().add(getAuthor(id, "Test Author"));
        bookOne.getAuthors().add(authorOne);
        bookOne.getAuthors().add(authorTwo);
        Book bookTwo = new Book();
        bookTwo.getAuthors().add(getAuthor(id, "Test Author"));
        Book bookThree = new Book();
        bookThree.getAuthors().add(authorOne);
        bookThree.getAuthors().add(getAuthor(id, "Test Author"));
        List<Book> books = Arrays.asList(bookOne, bookTwo, bookThree);
        InOrder inOrder = inOrder(authorDao, bookDao, authorDao, bookDao);
        when(authorDao.findById(id)).thenReturn(Optional.of(updatedAuthor));
        when(bookDao.findByAuthors(updatedAuthor)).thenReturn(books);
        String updatedAuthorName = "UPDATED Test Author";

        authorService.update(id, updatedAuthorName);
        inOrder.verify(authorDao).findById(id);
        inOrder.verify(bookDao).findByAuthors(updatedAuthor);
        inOrder.verify(authorDao).save(updatedAuthor);
        inOrder.verify(bookDao).saveAll(books);
        assertThat(updatedAuthor.getFullName()).isEqualTo(updatedAuthorName);
        assertThat(bookOne.getAuthors()).contains(updatedAuthor);
        assertThat(bookTwo.getAuthors()).contains(updatedAuthor);
        assertThat(bookThree.getAuthors()).contains(updatedAuthor);
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
        Optional<Author> optionalAuthor = Optional.of(getAuthor("id", "Test Author"));
        InOrder inOrder = inOrder(authorDao, bookDao, authorDao);
        when(authorDao.findById("id")).thenReturn(optionalAuthor);
        when(bookDao.findByAuthors(optionalAuthor.get())).thenReturn(new ArrayList<>());

        authorService.delete("id");
        inOrder.verify(authorDao).findById("id");
        inOrder.verify(bookDao).findByAuthors(optionalAuthor.get());
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
        Book book = new Book();
        List<Book> books = new ArrayList<>();
        books.add(book);
        Optional<Author> optionalAuthor = Optional.of(getAuthor("id", "Test Author"));
        when(authorDao.findById("id")).thenReturn(optionalAuthor);
        when(bookDao.findByAuthors(optionalAuthor.get())).thenReturn(books);
        assertThatExceptionOfType(BiblioServiceException.class)
                .isThrownBy(() -> authorService.delete("id"))
                .withMessage("AUTHOR WASN'T REMOVED DUE TO SOME BOOKS HAVE LINK TO THIS AUTHOR\n" +
                        "REMOVE THIS AUTHOR FROM THE BOOKS FIRST");
    }
}