package ru.otus.training.alekseimorozov.bibliootus.biblioshell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.BiblioRunner;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.List;

@ShellComponent
public class BiblioShell {
    private BiblioRunner runner;

    @Autowired
    public BiblioShell(BiblioRunner runner) {
        this.runner = runner;
    }

    @ShellMethod("Show list of all books or book with given id")
    public void showBooks(@ShellOption(defaultValue = "0") Long bookId) {
        if (bookId == 0) {
            printAllBooks(runner.showAllBooks());
        } else {
            printBook(runner.findBookById(bookId));
        }
    }

    @ShellMethod(value = "Find books by part of name")
    public void findBooksByName(@ShellOption(value = {"-name", "--name"}, defaultValue = "") String name) {
        if (name.isEmpty()) {
            printAllBooks(runner.showAllBooks());
        } else {
            printAllBooks(runner.findBooksByName(name));
        }
    }

    @ShellMethod(value = "Show list of books with certain genre", key = "books-with-genre")
    public void showBooksWithGenre(@ShellOption(value = {"-id", "--id"}, defaultValue = "0") Long genreId) {
        if (genreId > 0) {
            printAllBooks(runner.findBooksByGenre(genreId));
        } else {
            printAllBooks(runner.showAllBooks());
        }
    }

    @ShellMethod(value = "Show list of books with certain author", key = "books-by-author-id")
    public void findBooksByAuthorId(@ShellOption(value = {"-id", "--id"}, defaultValue = "0") Long authorId) {
        if (authorId > 0) {
            printAllBooks(runner.findBooksByAuthorId(authorId));
        } else {
            printAllBooks(runner.showAllBooks());
        }
    }

    @ShellMethod(value = "Show list of books with certain author", key = "books-by-author-name")
    public void findBooksByAuthorName(@ShellOption(value = {"-name", "--name"}, defaultValue = "") String authorName) {
        if (authorName.isEmpty()) {
            printAllBooks(runner.showAllBooks());
        } else {
            printAllBooks(runner.findBooksByAuthorName(authorName));
        }
    }

    @ShellMethod("Add new book to library")
    public void addBook(@ShellOption(value = {"-name", "--name"}, help = "define name of book") String bookName,
                        @ShellOption(value = {"-gid", "--gid"}, help = "define id of genre") Long genreId,
                        @ShellOption(value = {"-aid", "--aid"}, help = "define author's id") Long authorId) {
        printBook(runner.addBook(bookName, genreId, authorId));
    }

    @ShellMethod("Update genre of book")
    public void updateBookGenre(@ShellOption(value = {"-bid", "--bid"}, help = "define id of book") Long bookId,
                                @ShellOption(value = {"-gid", "--gid"}, help = "define id of new genre") Long genreId) {
        runner.updateBookGenre(bookId, genreId);
        System.out.println("Book genre was updated");
    }

    @ShellMethod("Update name of book")
    public void updateBookName(@ShellOption(value = {"-bid", "--bid"}, help = "define id of book") Long bookId,
                               @ShellOption(value = {"-name", "--name"}) String name) {
        runner.updateBookName(bookId, name);
        System.out.println("Book name was updated");
    }

    @ShellMethod("Add new author to book's list of authors")
    public void addAuthorToBook(@ShellOption(value = {"-bid", "--bid"}, help = "define id of book") Long bookId,
                                @ShellOption(value = {"-aid", "--aid"}, help = "define id of author to add") Long authorId) {
        runner.addAuthorToBook(bookId, authorId);
        System.out.println("Author was added to book");
    }

    @ShellMethod("Remove author from book's list of authors")
    public void delAuthorFromBook(@ShellOption(value = {"-bid", "--bid"}, help = "define id of book") Long bookId,
                                  @ShellOption(value = {"-aid", "--aid"}, help = "define id of author to remove") Long authorId) {
        runner.delAuthorFromBook(bookId, authorId);
        System.out.println("Author was removed from book");
    }

    @ShellMethod(value = "Remove book from library", key = "del-book")
    public void deleteBook(@ShellOption(value = {"-id", "--id"}, help = "define id of book") Long id) {
        runner.deleteBook(id);
        System.out.println("Book was removed");
    }

    @ShellMethod(value = "Show all authors or author with certain ID")
    public void showAuthors(@ShellOption(defaultValue = "0") Long id) {
        if (id > 0) {
            printAuthor(runner.findAuthorById(id));
        } else {
            printAllAuthors(runner.showAllAuthors());
        }
    }

    @ShellMethod(value = "Find authors by part of name")
    public void findAuthorsByName(@ShellOption(defaultValue = "") String name) {
        if (name.isEmpty()) {
            printAllAuthors(runner.showAllAuthors());
        } else {
            printAllAuthors(runner.findAuthorByName(name));
        }
    }


    @ShellMethod("Add new author to library")
    public void addAuthor(String name) {
        runner.addAuthor(name);
        System.out.println("New author was added");
    }

    @ShellMethod("Remove author from library")
    public void delAuthor(Long id) {
        runner.deleteAuthor(id);
        System.out.println("Author was removed");
    }

    @ShellMethod("Add new author to library")
    public void updateAuthor(@ShellOption(help = "id of authors, which will be updated") Long id,
                             @ShellOption(help = "new author's name") String name) {
        runner.updateAuthor(id, name);
        System.out.println("author was updated");
    }

    @ShellMethod("Add new genre to library")
    public void addGenre(String name) {
        runner.addGenre(name);
        System.out.println("new genre was added");
    }

    @ShellMethod("Show all genres in library")
    public void showGenres() {
        printAllGenre(runner.showAllGenres());
    }

    @ShellMethod("Remove genre from library")
    public void delGenre(Long id) {
        runner.deleteGenre(id);
        System.out.println("genre was deleted");
    }

    @ShellMethod("Update genre")
    public void updateGenre(@ShellOption(help = "id of genre, wich will be updated") Long id, @ShellOption(help =
            "new name of genre") String name) {
        runner.updateGenre(id, name);
        System.out.println("Genre was updated");
    }

    private void printAllBooks(List<Book> books) {
        for (Book book : books) {
            printBook(book);
        }
    }

    private void printBook(Book book) {
        if (book != null) {
            System.out.println(book);
        } else {
            System.out.println("Book was not found");
        }
        System.out.println("------------------------");
    }

    private void printAllAuthors(List<Author> authors) {
        for (Author author : authors) {
            printAuthor(author);
        }
    }

    private void printAuthor(Author author) {
        System.out.println(author);
        System.out.println("------------------------");
    }

    private void printAllGenre(List<Genre> genres) {
        for (Genre genre : genres) {
            printGenre(genre);
        }
    }

    private void printGenre(Genre genre) {
        System.out.println(genre);
        System.out.println("------------------------");
    }
}