package ru.otus.training.alekseimorozov.bibliootus.biblioshell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.AuthorService;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.BookCommentService;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.BookService;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.GenreService;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.serviceexception.BiblioServiceException;

import static ru.otus.training.alekseimorozov.bibliootus.entity.EntityPrinter.*;

@ShellComponent
public class BiblioShell {
    private static final String ERR_MSG = "%1$s WASN'T REMOVED DUE TO SOME BOOKS HAVE LINK TO THIS %1$s \n" +
            "REMOVE %1$s FROM THE BOOKS FIRST";

    private AuthorService authorService;
    private GenreService genreService;
    private BookService bookService;
    private BookCommentService bookCommentService;

    public BiblioShell(AuthorService authorService, GenreService genreService, BookService bookService, BookCommentService bookCommentService) {
        this.authorService = authorService;
        this.genreService = genreService;
        this.bookService = bookService;
        this.bookCommentService = bookCommentService;
    }

    @ShellMethod("Show list of all books or book with given id")
    public String showBooks(@ShellOption(defaultValue = "0") Long bookId) {
        return execute(() -> bookId == 0 ? printAllBooks(bookService.readAll()) :
                printBook(bookService.readById(bookId), String.format("show book with id: %d \n", bookId)));
    }

    @ShellMethod(value = "Find books by part of name")
    public String findBooksByName(@ShellOption(value = {"-name", "--name"}, defaultValue = "") String name) {
        return execute(() -> name.isEmpty() ? printAllBooks(bookService.readAll()) :
                printAllBooks(bookService.findByName(name), String.format("find book by part of name: %s \n", name)));
    }

    @ShellMethod(value = "Show list of books with certain genre", key = "books-with-genre")
    public String showBooksWithGenre(@ShellOption(value = {"-id", "--id"}, defaultValue = "0") Long genreId) {
        return execute(() -> genreId > 0 ? printAllBooks(bookService.findByGenreId(genreId)) :
                printAllBooks(bookService.readAll()));
    }

    @ShellMethod(value = "Show list of books with certain author", key = "books-by-author-id")
    public String findBooksByAuthorId(@ShellOption(value = {"-id", "--id"}, defaultValue = "0") Long authorId) {
        return execute(() -> authorId > 0 ? printAllBooks(bookService.findByAuthorId(authorId)) :
                printAllBooks(bookService.readAll()));
    }

    @ShellMethod(value = "Show list of books with certain author", key = "books-by-author-name")
    public String findBooksByAuthorName(@ShellOption(value = {"-name", "--name"}, defaultValue = "") String authorName) {
        return execute(() -> authorName.isEmpty() ? printAllBooks(bookService.readAll()) :
                printAllBooks(bookService.findByAuthorName(authorName),
                        String.format("show all books with Author's part of name: %s \n", authorName)));
    }

    @ShellMethod("Add new book to library")
    public String addBook(@ShellOption(value = {"-title", "--title"}, help = "define title of book") String title,
                          @ShellOption(value = {"-gid", "--gid"}, help = "define id of genre", defaultValue = "0") Long genreId,
                          @ShellOption(value = {"-aid", "--aid"}, help = "define author's id", defaultValue = "0") Long authorId) {
        return execute(() -> printBook(bookService.create(title, genreId, authorId), "book was added \n"));
    }

    @ShellMethod("Update genre of book")
    public String updateBookGenre(@ShellOption(value = {"-bid", "--bid"}, help = "define id of book") Long bookId,
                                  @ShellOption(value = {"-gid", "--gid"}, help = "define id of new genre") Long genreId) {
        return execute(() -> {
            bookService.updateBookGenre(bookId, genreId);
            return "Book genre was updated";
        });
    }

    @ShellMethod("Update title of book")
    public String updateBookTitle(@ShellOption(value = {"-bid", "--bid"}, help = "define id of book") Long bookId,
                                  @ShellOption(value = {"-title", "--title"}) String title) {
        return execute(() -> {
            bookService.updateBookName(bookId, title);
            return "Book name was updated";
        });
    }

    @ShellMethod("Add new author to book's list of authors")
    public String addAuthorToBook(@ShellOption(value = {"-bid", "--bid"}, help = "define id of book") Long bookId,
                                  @ShellOption(value = {"-aid", "--aid"}, help = "define id of author to add") Long authorId) {
        return execute(() -> {
            bookService.addAuthorToBook(bookId, authorId);
            return "Author was added to book";
        });
    }

    @ShellMethod("Remove author from book's list of authors")
    public String delAuthorFromBook(@ShellOption(value = {"-bid", "--bid"}, help = "define id of book") Long bookId,
                                    @ShellOption(value = {"-aid", "--aid"}, help = "define id of author to remove") Long authorId) {
        return execute(() -> {
            bookService.removeAuthorFromBook(bookId, authorId);
            return "Author was removed from book";
        });
    }

    @ShellMethod(value = "Remove book from library", key = "del-book")
    public String deleteBook(@ShellOption(value = {"-id", "--id"}, help = "define id of book") Long id) {
        return execute(() -> {
            bookService.delete(id);
            return "Book was removed";
        });
    }

    @ShellMethod(value = "add comment tu book")
    public String addComment(@ShellOption(help = "define id of book") Long bookId,
                             @ShellOption(value = {"-c", "--c"}, help = "text of comment") String comment) {
        return execute(() -> {
            bookCommentService.addComment(bookId, comment);
            return "Comment saved";
        });
    }

    @ShellMethod(value = "show all comments or comment for required id")
    public String showComment(@ShellOption(defaultValue = "0") Long id) {
        return execute(() -> id == 0 ? printAllComments(bookCommentService.readAll()) :
                printComment(bookCommentService.readById(id)));
    }

    @ShellMethod(value = "show all comments for book with required id")
    public String showCommentForBook(Long id) {
        return printAllComments(bookCommentService.readByBookId(id));
    }

    @ShellMethod(value = "update text of comment")
    public String updateCommentText(Long id, String text) {
        return execute(() -> {
            bookCommentService.updateText(id, text);
            return "Comment was updated";
        });
    }

    @ShellMethod(value = "link comment to other book")
    public String updateCommentBook(@ShellOption(value = {"-cid", "--cid"}, help = "id of comment to id") Long commentId,
                                    @ShellOption(value = {"-bid, --bid"}, help = "new book id") Long bookId) {
        return execute(() -> {
            bookCommentService.updateBook(commentId, bookId);
            return "Comment was updated";
        });
    }

    @ShellMethod(value = "delete comment")
    public String delComment(Long id) {
        return execute(() -> {
            bookCommentService.delete(id);
            return "Comment was deteted";
        });
    }

    @ShellMethod(value = "Show all authors or author with certain ID")
    public String showAuthors(@ShellOption(defaultValue = "0") Long id) {
        return execute(() -> id > 0 ? printAuthor(authorService.findById(id)) :
                printAllAuthors(authorService.readAll()));
    }

    @ShellMethod(value = "Find authors by part of name")
    public String findAuthorsByName(@ShellOption(defaultValue = "") String name) {
        return execute(() -> name.isEmpty() ? printAllAuthors(authorService.readAll()) :
                printAllAuthors(authorService.findByName(name)));
    }

    @ShellMethod("Add new author to library")
    public String addAuthor(String name) {
        authorService.create(name);
        return "New author was added";
    }

    @ShellMethod("Remove author from library")
    public String delAuthor(Long id) {
        return execute(() -> {
            authorService.delete(id);
            return "Author was removed";
        });
    }

    @ShellMethod("Update author's name")
    public String updateAuthor(@ShellOption(help = "id of authors, which will be updated") Long id,
                               @ShellOption(help = "new author's name") String name) {
        return execute(() -> {
            authorService.update(id, name);
            return "author was updated";
        });
    }

    @ShellMethod("Add new genre to library")
    public String addGenre(String name) {
        genreService.create(name);
        return "new genre was added";
    }

    @ShellMethod("Show all genres in library")
    public String showGenres(@ShellOption(defaultValue = "0") Long id) {
        return execute(() -> id > 0 ? printGenre(genreService.readById(id)) : printAllGenre(genreService.readAll()));
    }

    @ShellMethod("Remove genre from library")
    public String delGenre(Long id) {
        return execute(() -> {
            genreService.delete(id);
            return "genre was deleted";
        });
    }

    @ShellMethod("Update genre")
    public String updateGenre(@ShellOption(help = "id of genre, wich will be updated") Long id, @ShellOption(help =
            "new name of genre") String name) {
        return execute(() -> {
            genreService.update(id, name);
            return "Genre was updated";
        });
    }

    private String execute(Runner runner) {
        try {
            return runner.run();
        } catch (BiblioServiceException e) {
            return e.getMessage();
        }
    }

    private interface Runner {
        String run();
    }
}