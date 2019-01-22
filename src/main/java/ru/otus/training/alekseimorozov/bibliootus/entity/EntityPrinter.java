package ru.otus.training.alekseimorozov.bibliootus.entity;

import java.util.List;

public class EntityPrinter {
    private static final String FOOTER = "\n------------------------\n";

    public static String printAllBooks(List<Book> books) {
        StringBuilder result = new StringBuilder();
        for (Book book : books) {
            result.append(printBook(book));
        }
        return result.toString();
    }

    public static String printBook(Book book) {
        StringBuilder result = new StringBuilder();
        if (book != null) {
            result.append(book);
        } else {
            result.append("Book was not found");
        }
        return result.append(FOOTER).toString();
    }

    public static String printAllAuthors(List<Author> authors) {
        StringBuilder result = new StringBuilder();
        for (Author author : authors) {
            result.append(printAuthor(author));
        }
        return result.toString();
    }

    public static String printAuthor(Author author) {
        return author + FOOTER;
    }

    public static String printAllGenre(List<Genre> genres) {
        StringBuilder result = new StringBuilder();
        for (Genre genre : genres) {
            result.append(printGenre(genre));
        }
        return result.toString();
    }

    public static String printGenre(Genre genre) {
        return genre + FOOTER;
    }

    public static String printAllComments(List<BookComment> comments) {
        StringBuilder result = new StringBuilder();
        for (BookComment comment : comments) {
            result.append(printComment(comment));
        }
        return result.toString();
    }

    public static String printComment(BookComment comment) {
        return comment + FOOTER;
    }
}