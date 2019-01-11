package ru.otus.training.alekseimorozov.bibliootus.entity;

import java.io.Serializable;

public class Genre implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        return String.format("%s (id: %d)", getName(), getId());
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
        return id == genre.id && name != null && name.equals(genre.name);
    }

    @Override
    public int hashCode() {
        int result = 31 * 19 + (int) (id ^ (id >>> 32));
        return name == null ? result : 31 * result + name.hashCode();
    }

    public static Genre getGenre(Long id, String name) {
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(name);
        return genre;
    }
}