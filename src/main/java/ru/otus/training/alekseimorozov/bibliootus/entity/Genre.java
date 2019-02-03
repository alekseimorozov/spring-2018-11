package ru.otus.training.alekseimorozov.bibliootus.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Objects;

@Document(collection = "genres")
public class Genre implements Serializable {
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String name;

    public Genre() {
    }

    public Genre(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%s (id: %s)", getName(), getId());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Genre)) {
            return false;
        }
        Genre genre = (Genre) o;
        if (name == null && genre.name == null) {
            return id == genre.id;
        }
        return Objects.equals(id, genre.id) && Objects.equals(name, genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public static Genre getGenre(String id, String name) {
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(name);
        return genre;
    }
}