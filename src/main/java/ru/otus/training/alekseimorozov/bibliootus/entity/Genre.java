package ru.otus.training.alekseimorozov.bibliootus.entity;

public class Genre extends CommonEntity {
    private static final long serialVersionUID = 1L;

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
        if (getName() == null && genre.getName() == null) {
            return getId() == genre.getId();
        }
        return getId() == genre.getId() && getName() != null && getName().equals(genre.getName());
    }

    @Override
    public int hashCode() {
        int result = 31 * 19 + (int) (getId() ^ (getId() >>> 32));
        return getName() == null ? result : 31 * result + getName().hashCode();
    }

    public static Genre getGenre(Long id, String name) {
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(name);
        return genre;
    }
}