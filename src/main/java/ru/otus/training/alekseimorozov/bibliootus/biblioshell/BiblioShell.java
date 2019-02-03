package ru.otus.training.alekseimorozov.bibliootus.biblioshell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.AuthorService;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.BookService;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.GenreService;
import ru.otus.training.alekseimorozov.bibliootus.businesslogic.serviceexception.BiblioServiceException;

import static ru.otus.training.alekseimorozov.bibliootus.entity.EntityPrinter.*;

@ShellComponent
public class BiblioShell {
    private AuthorService authorService;
    private GenreService genreService;
    private BookService bookService;

    public BiblioShell(AuthorService authorService, GenreService genreService, BookService bookService) {
        this.authorService = authorService;
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @ShellMethod("Show list of all books or book with given id")
    public String showBooks(@ShellOption(defaultValue = "") String bookId) {
        return execute(() -> bookId.isEmpty() ? printAllBooks(bookService.readAll()) :
                printBook(bookService.readById(bookId), String.format("show book with id: %s \n", bookId)));
    }

    @ShellMethod(value = "Find books by part of name")
    public String findBooksByName(@ShellOption(value = {"-name", "--name"}, defaultValue = "") String name) {
        return execute(() -> name.isEmpty() ? printAllBooks(bookService.readAll()) :
                printAllBooks(bookService.findByName(name), String.format("find book by part of name: %s \n", name)));
    }

    @ShellMethod(value = "Show list of books with certain genre", key = "books-with-genre")
    public String showBooksWithGenre(@ShellOption(value = {"-id", "--id"}, defaultValue = "") String genreId) {
        return execute(() -> genreId.isEmpty() ? printAllBooks(bookService.readAll()) :
                printAllBooks(bookService.findByGenreId(genreId)));
    }

    @ShellMethod(value = "Show list of books with certain author", key = "books-by-author-id")
    public String findBooksByAuthorId(@ShellOption(value = {"-id", "--id"}, defaultValue = "") String authorId) {
        return execute(() -> authorId.isEmpty() ?printAllBooks(bookService.readAll()) :
                printAllBooks(bookService.findByAuthorId(authorId)));
    }

    @ShellMethod(value = "Show list of books with certain author", key = "books-by-author-name")
    public String findBooksByAuthorName(@ShellOption(value = {"-name", "--name"}, defaultValue = "") String authorName) {
        return execute(() -> authorName.isEmpty() ? printAllBooks(bookService.readAll()) :
                printAllBooks(bookService.findByAuthorName(authorName),
                        String.format("show all books with Author's part of name: %s \n", authorName)));
    }

    @ShellMethod("Add new book to library")
    public String addBook(@ShellOption(value = {"-title", "--title"}, help = "define title of book") String title,
                          @ShellOption(value = {"-gid", "--gid"}, help = "define id of genre", defaultValue = "") String genreId,
                          @ShellOption(value = {"-aid", "--aid"}, help = "define author's id", defaultValue = "") String authorId) {
        return execute(() -> printBook(bookService.create(title, genreId, authorId), "book was added \n"));
    }

    @ShellMethod("Update genre of book")
    public String updateBookGenre(@ShellOption(value = {"-bid", "--bid"}, help = "define id of book") String bookId,
                                  @ShellOption(value = {"-gid", "--gid"}, help = "define id of new genre") String genreId) {
        return execute(() -> {
            bookService.updateBookGenre(bookId, genreId);
            return "Book genre was updated";
        });
    }

    @ShellMethod("Update title of book")
    public String updateBookTitle(@ShellOption(value = {"-bid", "--bid"}, help = "define id of book") String bookId,
                                  @ShellOption(value = {"-title", "--title"}) String title) {
        return execute(() -> {
            bookService.updateBookName(bookId, title);
            return "Book name was updated";
        });
    }

    @ShellMethod("Add new author to book's list of authors")
    public String addAuthorToBook(@ShellOption(value = {"-bid", "--bid"}, help = "define id of book") String bookId,
                                  @ShellOption(value = {"-aid", "--aid"}, help = "define id of author to add") String authorId) {
        return execute(() -> {
            bookService.addAuthorToBook(bookId, authorId);
            return "Author was added to book";
        });
    }

    @ShellMethod("Remove author from book's list of authors")
    public String delAuthorFromBook(@ShellOption(value = {"-bid", "--bid"}, help = "define id of book") String bookId,
                                    @ShellOption(value = {"-aid", "--aid"}, help = "define id of author to remove") String authorId) {
        return execute(() -> {
            bookService.removeAuthorFromBook(bookId, authorId);
            return "Author was removed from book";
        });
    }

    @ShellMethod(value = "Remove book from library", key = "del-book")
    public String deleteBook(@ShellOption(value = {"-id", "--id"}, help = "define id of book") String id) {
        return execute(() -> {
            bookService.delete(id);
            return "Book was removed";
        });
    }

    @ShellMethod(value = "add comment tu book")
    public String addComment(@ShellOption(help = "define id of book") String bookId,
                             @ShellOption(value = {"-c", "--c"}, help = "text of comment") String comment) {
        return execute(() -> {
            bookService.addComment(bookId, comment);
            return "Comment saved";
        });
    }

    @ShellMethod(value = "show all comments for book with required id")
    public String showCommentForBook(String id) {
        return printAllComments(bookService.readCommentsByBookId(id));
    }

    @ShellMethod(value = "update text of comment")
    public String updateCommentText(String bookId, int commentId, String text) {
        return execute(() -> {
            bookService.updateComment(bookId, commentId, text);
            return "Comment was updated";
        });
    }

    @ShellMethod(value = "delete comment")
    public String delComment(String bookId, int commentId) {
        return execute(() -> {
            bookService.deleteComment(bookId, commentId);
            return "Comment was deteted";
        });
    }

    @ShellMethod(value = "Show all authors or author with certain ID")
    public String showAuthors(@ShellOption(defaultValue = "") String id) {
        return execute(() -> id.isEmpty() ? printAllAuthors(authorService.readAll()) :
                printAuthor(authorService.findById(id)));
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
    public String delAuthor(String id) {
        return execute(() -> {
            authorService.delete(id);
            return "Author was removed";
        });
    }

    @ShellMethod("Update author's name")
    public String updateAuthor(@ShellOption(help = "id of authors, which will be updated") String id,
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
    public String showGenres(@ShellOption(defaultValue = "") String id) {
        return execute(() -> id.isEmpty() ? printAllGenre(genreService.readAll()) :
                printGenre(genreService.readById(id)));
    }

    @ShellMethod("Remove genre from library")
    public String delGenre(String id) {
        return execute(() -> {
            genreService.delete(id);
            return "genre was deleted";
        });
    }

    @ShellMethod("Update genre")
    public String updateGenre(@ShellOption(help = "id of genre, which will be updated") String id,
                              @ShellOption(help = "new name of genre") String name) {
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