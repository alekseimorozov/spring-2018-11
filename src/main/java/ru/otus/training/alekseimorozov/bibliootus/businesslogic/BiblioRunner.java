package ru.otus.training.alekseimorozov.bibliootus.businesslogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.service.AuthorService;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.service.BookService;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.service.GenreService;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.ArrayList;
import java.util.List;


@Service
public class BiblioRunner {
    private AuthorService authorService;
    private GenreService genreService;
    private BookService bookService;

    @Autowired
    public BiblioRunner(AuthorService authorService, GenreService genreService, BookService bookService) {
        this.authorService = authorService;
        this.genreService = genreService;
        this.bookService = bookService;
    }

    public List<Book> showAllBooks() {
        List<Book> books = bookService.readAll();
        setAuthorsForAll(books);
        return books;
    }

    public Book findBookById(Long bookId) {
        System.out.println("show book by id " + bookId);
        Book book = bookService.readById(bookId);
        fillListOfAuthor(book);
        return book;
    }

    public List<Book> findBooksByName(String name) {
        System.out.println("find book by name " + name);
        List<Book> books = bookService.findByName(name);
        setAuthorsForAll(books);
        return books;
    }

    public List<Book> findBooksByGenre(Long genreId) {
        System.out.println("show all books with genre\n");
        List<Book> books = bookService.findByGenreId(genreId);
        setAuthorsForAll(books);
        return books;
    }

    public List<Book> findBooksByAuthorId(Long authorId) {
        System.out.println("show all books with Author\n");
        List<Book> books = bookService.findByAuthorId(authorId);
        setAuthorsForAll(books);
        return books;
    }

    public List<Book> findBooksByAuthorName(String authorName) {
        System.out.println("show all books with Author " + authorName + "\n");
        List<Book> books = bookService.findByAuthorName(authorName);
        setAuthorsForAll(books);
        return books;
    }

    public Book addBook(String bookName, Long genreId, Long authorId) {
        Book book = new Book();
        book.setName(bookName);
        book.setGenre(findGenreById(genreId));
        List<Author> authors = new ArrayList<>();
        authors.add(findAuthorById(authorId));
        book.setAuthors(authors);
        System.out.println("book was added");
        return bookService.create(book);
    }

    public void updateBookGenre(Long bookId, Long genreId) {
        bookService.updateBookGenre(bookId, genreId);
    }

    public void updateBookName(Long bookId, String name) {
        bookService.updateBookName(bookId, name);
    }

    public void addAuthorToBook(Long bookId, Long authorId) {
        bookService.addAuthorToBook(bookId, authorId);
    }

    public void delAuthorFromBook(Long bookId, Long authorId) {
        bookService.removeAuthorFromBook(bookId, authorId);
    }

    public void deleteBook(Long id) {
        bookService.delete(id);
    }

    private void setAuthorsForAll(List<Book> books) {
        for (Book book : books) {
            fillListOfAuthor(book);
        }
    }

    private void fillListOfAuthor(Book book) {
        if (book != null) {
            book.setAuthors(authorService.findByBook(book.getId()));
        }
    }

    public List<Author> showAllAuthors() {
        return authorService.readAll();
    }


    public List<Author> findAuthorByName(String name) {
        return authorService.findByName(name);
    }

    public void updateAuthor(Long id, String name) {
        authorService.update(id, name);
    }

    public Author findAuthorById(Long authorId) {
        return authorService.findById(authorId);
    }

    public void addAuthor(String name) {
        authorService.create(name);
    }

    public void deleteAuthor(Long id) {
        authorService.delete(id);
    }

    public void addGenre(String name) {
        genreService.create(name);
    }

    public List<Genre> showAllGenres() {
        return genreService.readAll();
    }

    public void updateGenre(Long id, String name) {
        genreService.update(id, name);
        System.out.println("genre was updated");
    }

    public Genre findGenreById(Long genreId) {
        return genreService.readById(genreId);
    }

    public void deleteGenre(Long id) {
        genreService.delete(id);
    }
}