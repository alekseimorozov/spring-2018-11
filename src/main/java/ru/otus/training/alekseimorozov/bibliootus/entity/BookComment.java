package ru.otus.training.alekseimorozov.bibliootus.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "book_comments")
public class BookComment implements Serializable {
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "book_id")
    private Book book;
    private String comment;

    public BookComment() {
    }

    public BookComment(String comment) {
        this.comment = comment;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, comment);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof BookComment)) {
            return false;
        }
        BookComment bookComment = (BookComment) obj;
        return Objects.equals(id, bookComment.id) && Objects.equals(book, bookComment.book) &&
                Objects.equals(comment, bookComment.comment);
    }

    @Override
    public String toString() {
        return String.format("id: %d\n\t%s\n", id, comment);
    }
}