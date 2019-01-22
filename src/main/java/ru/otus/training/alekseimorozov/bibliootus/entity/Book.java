package ru.otus.training.alekseimorozov.bibliootus.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Book implements Serializable {
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "author_to_book_map",
            joinColumns = @JoinColumn(name = "book_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "author_id", nullable = false)
    )
    private List<Author> authors = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "genre_id")
    private Genre genre;

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
        return Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(genre, book.genre) &&
                Objects.deepEquals(authors.toArray(), book.authors.toArray());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, genre);
    }
}