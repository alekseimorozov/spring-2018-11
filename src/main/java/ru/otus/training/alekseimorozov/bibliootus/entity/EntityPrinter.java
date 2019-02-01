package ru.otus.training.alekseimorozov.bibliootus.entity;

import java.util.List;

public class EntityPrinter {
    private static final String FOOTER = "\n------------------------\n";

    public static String printAllBooks(List<Book> books, String... messages) {
        StringBuilder result = new StringBuilder();
        for (Book book : books) {
            result.append(printBook(book));
        }
        return messages.length == 0 ? result.toString() : messages[0] + result.toString();
    }

    public static String printBook(Book book, String... messages) {
        StringBuilder result = new StringBuilder();
        if (book != null) {
            result.append(book);
        } else {
            result.append("Book was not found");
        }
        return messages.length == 0 ? result.append(FOOTER).toString() : messages[0] + result.append(FOOTER).toString();
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

    public static String printAllComments(List<String> comments) {
        StringBuilder result = new StringBuilder();
        for (int i = 0;  i < comments.size(); i++) {
            result.append(printComment(comments.get(i), i));
        }
        return result.toString();
    }

    public static String printComment(String comment, int i) {
        return comment + " (id: " + i + ")" + FOOTER;
    }
}