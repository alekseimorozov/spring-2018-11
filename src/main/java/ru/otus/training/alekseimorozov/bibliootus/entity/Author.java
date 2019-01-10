package ru.otus.training.alekseimorozov.bibliootus.entity;

public class Author extends CommonEntity {
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
        if (!(o instanceof Author)) {
            return false;
        }
        Author author = (Author) o;
        if (getName() == null && author.getName() == null) {
            return getId() == author.getId();
        }
        return getId() == author.getId() && getName() != null && getName().equals(author.getName());
    }

    @Override
    public int hashCode() {
        int result = 31 * 19 + (int) (getId() ^ (getId() >>> 32));
        return getName() == null ? result : 31 * result + getName().hashCode();
    }

    public static Author getAuthor(Long id, String name) {
        Author author = new Author();
        author.setId(id);
        author.setName(name);
        return author;
    }
}