package ru.otus.training.alekseimorozov.bibliootus.entity;

import java.io.Serializable;

public class Author implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String fullName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return String.format("%s (id: %d)", getFullName(), getId());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Author)) {
            return false;
        }
        Author author = (Author) o;
        if (fullName == null && author.fullName == null) {
            return id == author.id;
        }
        return id == author.id && fullName != null && fullName.equals(author.fullName);
    }

    @Override
    public int hashCode() {
        int result = 31 * 19 + (int) (id ^ (id >>> 32));
        return fullName == null ? result : 31 * result + fullName.hashCode();
    }

    public static Author getAuthor(Long id, String name) {
        Author author = new Author();
        author.setId(id);
        author.setFullName(name);
        return author;
    }
}