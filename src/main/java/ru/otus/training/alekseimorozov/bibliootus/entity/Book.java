package ru.otus.training.alekseimorozov.bibliootus.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private List<Author> authors = new ArrayList<>();
    private Genre genre;

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Book title: ").append(title).append("(id: ").append(id).append(")\n")
                .append("genre: ").append(genre).append("\n")
                .append("authors: ").append("\n");
        for (Author author : authors) {
            result.append(author).append("\n");
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }
        Book book = (Book) o;
        boolean namesIsEqual = title == null && book.title == null ? true : title != null && title.equals(book.title);
        boolean genreIsEquals = genre == null && book.genre == null ? true : genre != null && genre.equals(book.genre);
        boolean authorsIsEqueal = Arrays.equals(authors.toArray(), book.authors.toArray());
        return getId() == book.getId() && namesIsEqual && genreIsEquals && authorsIsEqueal;
    }

    @Override
    public int hashCode() {
        int result = 31 * 19 + (int) (id ^ (id >>> 32));
        if (title != null) {
            result = 31 * result + title.hashCode();
        }
        return genre == null ? result : 31 * result + genre.hashCode();
    }
}