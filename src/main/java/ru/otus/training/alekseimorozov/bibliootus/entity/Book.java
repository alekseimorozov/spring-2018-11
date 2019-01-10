package ru.otus.training.alekseimorozov.bibliootus.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Book extends CommonEntity {
    private static final long serialVersionUID = 1L;

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
        result.append("Book name: ").append(getName()).append("(id: ").append(getId()).append(")\n")
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
        boolean namesIsEqual = getName() == null && book.getName() == null ? true :
                getName() != null && getName().equals(book.getName());
        boolean genreIsEquals = genre == null && book.genre == null ? true : genre != null && genre.equals(book.genre);
        boolean authorsIsEqueal = Arrays.equals(authors.toArray(), book.authors.toArray());
        return getId() == book.getId() && namesIsEqual && genreIsEquals && authorsIsEqueal;
    }

    @Override
    public int hashCode() {
        int result = 31 * 19 + (int) (getId() ^ (getId() >>> 32));
        return getName() == null ? result : 31 * result + getName().hashCode();
    }
}