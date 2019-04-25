package ru.otus.training.alekseimorozov.bibliootus.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "books")
public class Book implements Serializable {
   private static final long serialVersionUID = 1L;

    private String title;
    @DBRef
    private List<Author> authors = new ArrayList<>();
    private Genre genre;
    private List<String> comments = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Book title: ").append(title).append("; genre: ").append(genre).append("; authors:");
        for (Author author : authors) {
            result.append(" ").append(author).append(",");
        }
        result.deleteCharAt(result.length() - 1);
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
        return Objects.equals(title, book.title) && Objects.equals(genre, book.genre) &&
                Objects.deepEquals(authors.toArray(), book.authors.toArray()) &&
                Objects.deepEquals(comments.toArray(), book.comments.toArray());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, genre);
    }
}