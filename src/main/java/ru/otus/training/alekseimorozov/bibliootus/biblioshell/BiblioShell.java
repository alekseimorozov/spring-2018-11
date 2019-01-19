package ru.otus.training.alekseimorozov.bibliootus.biblioshell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.AuthorService;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.BookCommentService;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.BookService;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.GenreService;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.BookComment;

import java.util.List;

import static ru.otus.training.alekseimorozov.bibliootus.entity.EntityPrinter.*;

@ShellComponent
public class BiblioShell {
    private static final String ERR_MSG = "%1$s WASN'T REMOVED DUE TO SOME BOOKS HAVE LINK TO THIS %1$s \n" +
            "REMOVE %1$s FROM THE BOOKS FIRST";

    private AuthorService authorService;
    private GenreService genreService;
    private BookService bookService;
    private BookCommentService bookCommentService;

    @Autowired
    public BiblioShell(AuthorService authorService, GenreService genreService, BookService bookService) {
        this.authorService = authorService;
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @ShellMethod("Show list of all books or book with given id")
    public String showBooks(@ShellOption(defaultValue = "0") Long bookId) {
        if (bookId == 0) {
            return printAllBooks(bookService.readAll());
        } else {
            return String.format("show book with id: %d \n", bookId) + printBook(bookService.readById(bookId));
        }
    }

    @ShellMethod(value = "Find books by part of name")
    public String findBooksByName(@ShellOption(value = {"-name", "--name"}, defaultValue = "") String name) {
        if (name.isEmpty()) {
            return printAllBooks(bookService.readAll());
        } else {
            return String.format("find book by part of name: %s \n", name) + printAllBooks(bookService.findByName(name));
        }
    }

    @ShellMethod(value = "Show list of books with certain genre", key = "books-with-genre")
    public String showBooksWithGenre(@ShellOption(value = {"-id", "--id"}, defaultValue = "0") Long genreId) {
        if (genreId > 0) {
            return printAllBooks(bookService.findByGenreId(genreId));
        } else {
            return printAllBooks(bookService.readAll());
        }
    }

    @ShellMethod(value = "Show list of books with certain author", key = "books-by-author-id")
    public String findBooksByAuthorId(@ShellOption(value = {"-id", "--id"}, defaultValue = "0") Long authorId) {
        if (authorId > 0) {
            return printAllBooks(bookService.findByAuthorId(authorId));
        } else {
            return printAllBooks(bookService.readAll());
        }
    }

    @ShellMethod(value = "Show list of books with certain author", key = "books-by-author-name")
    public String findBooksByAuthorName(@ShellOption(value = {"-name", "--name"}, defaultValue = "") String authorName) {
        if (authorName.isEmpty()) {
            return printAllBooks(bookService.readAll());
        } else {
            return String.format("show all books with Author's part of name: %s \n", authorName) + printAllBooks(bookService.findByAuthorName(authorName));
        }
    }

    @ShellMethod("Add new book to library")
    public String addBook(@ShellOption(value = {"-title", "--title"}, help = "define title of book") String title,
                          @ShellOption(value = {"-gid", "--gid"}, help = "define id of genre", defaultValue = "0") Long genreId,
                          @ShellOption(value = {"-aid", "--aid"}, help = "define author's id", defaultValue = "0") Long authorId) {
        Book book = new Book();
        book.setTitle(title);
        if (genreId > 0) {
            book.setGenre(genreService.readById(genreId));
        }
        if (authorId > 0) {
            book.getAuthors().add(authorService.findById(authorId));
        }
        return "book was added \n" + printBook(bookService.create(book));
    }

    @ShellMethod("Update genre of book")
    public String updateBookGenre(@ShellOption(value = {"-bid", "--bid"}, help = "define id of book") Long bookId,
                                  @ShellOption(value = {"-gid", "--gid"}, help = "define id of new genre") Long genreId) {
        bookService.updateBookGenre(bookId, genreId);
        return "Book genre was updated";
    }

    @ShellMethod("Update title of book")
    public String updateBookTitle(@ShellOption(value = {"-bid", "--bid"}, help = "define id of book") Long bookId,
                                 @ShellOption(value = {"-title", "--title"}) String title) {
        bookService.updateBookName(bookId, title);
        return "Book name was updated";
    }

    @ShellMethod("Add new author to book's list of authors")
    public String addAuthorToBook(@ShellOption(value = {"-bid", "--bid"}, help = "define id of book") Long bookId,
                                  @ShellOption(value = {"-aid", "--aid"}, help = "define id of author to add") Long authorId) {
        bookService.addAuthorToBook(bookId, authorId);
        return "Author was added to book";
    }

    @ShellMethod("Remove author from book's list of authors")
    public String delAuthorFromBook(@ShellOption(value = {"-bid", "--bid"}, help = "define id of book") Long bookId,
                                    @ShellOption(value = {"-aid", "--aid"}, help = "define id of author to remove") Long authorId) {
        bookService.removeAuthorFromBook(bookId, authorId);
        return "Author was removed from book";
    }

    @ShellMethod(value = "Remove book from library", key = "del-book")
    public String deleteBook(@ShellOption(value = {"-id", "--id"}, help = "define id of book") Long id) {
        bookService.delete(id);
        return "Book was removed";
    }

    @ShellMethod(value = "add comment tu book")
    public String addComment(@ShellOption(help = "define id of book") Long bookId,
                           @ShellOption(value = {"-c", "--c"}, help = "text of comment") String comment) {
        bookCommentService.addComment(bookId, comment);
        return  "Comment saved";
    }

    @ShellMethod(value = "show all comments or comment for required id")
    public String showComment(@ShellOption(defaultValue = "0") Long id) {
        if (id == 0) {
            return printAllComments(bookCommentService.readAll());
        } else {
            return printComment(bookCommentService.readById(id))
        }
    }

    @ShellMethod(value = "show all comments for book with required id")
    public String showCommentForBook(Long id) {
            return printComment(bookCommentService.readByBookId(id))
    }

    @ShellMethod(value = "update text of comment")
    public String updateCommentText(Long id, String text) {
        bookCommentService.updateText(id ,text);
        return "Comment was updated";
    }

    @ShellMethod(value = "link comment to other book")
    public String updateCommentBook(@ShellOption(value = {"-cid", "--cid"}, help = "id of comment to id") Long commentId,
                                    @ShellOption(value = {"-bid, --bid"}, help = "new book id") Long bookId) {
        bookCommentService.updateBook(commentId ,bookId);
        return "Comment was updated";
    }

    @ShellMethod(value = "delete comment")
    public String delComment(Long id) {
        bookCommentService.delete(id);
        return "Comment was deteted";
    }

    @ShellMethod(value = "Show all authors or author with certain ID")
    public String showAuthors(@ShellOption(defaultValue = "0") Long id) {
        if (id > 0) {
            return printAuthor(authorService.findById(id));
        } else {
            return printAllAuthors(authorService.readAll());
        }
    }

    @ShellMethod(value = "Find authors by part of name")
    public String findAuthorsByName(@ShellOption(defaultValue = "") String name) {
        if (name.isEmpty()) {
            return printAllAuthors(authorService.readAll());
        } else {
            return printAllAuthors(authorService.findByName(name));
        }
    }


    @ShellMethod("Add new author to library")
    public String addAuthor(String name) {
        authorService.create(name);
        return "New author was added";
    }

    @ShellMethod("Remove author from library")
    public String delAuthor(Long id) {
        try {
            authorService.delete(id);
            return "Author was removed";
        } catch (DataIntegrityViolationException e) {
            return String.format(ERR_MSG, "AUTHOR");
        }
    }

    @ShellMethod("Update author's name")
    public String updateAuthor(@ShellOption(help = "id of authors, which will be updated") Long id,
                               @ShellOption(help = "new author's name") String name) {
        authorService.update(id, name);
        return "author was updated";
    }

    @ShellMethod("Add new genre to library")
    public String addGenre(String name) {
        genreService.create(name);
        return "new genre was added";
    }

    @ShellMethod("Show all genres in library")
    public String showGenres(@ShellOption(defaultValue = "0") Long id) {
        if (id > 0) {
            return printGenre(genreService.readById(id));
        } else {
            return printAllGenre(genreService.readAll());
        }
    }

    @ShellMethod("Remove genre from library")
    public String delGenre(Long id) {
        try {
            genreService.delete(id);
            return "genre was deleted";
        } catch (DataIntegrityViolationException e) {
            return String.format(ERR_MSG, "GENRE");
        }
    }

    @ShellMethod("Update genre")
    public void updateGenre(@ShellOption(help = "id of genre, wich will be updated") Long id, @ShellOption(help =
            "new name of genre") String name) {
        genreService.update(id, name);
        System.out.println("Genre was updated");
    }
}