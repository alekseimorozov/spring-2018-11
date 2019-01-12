package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.otus.training.alekseimorozov.bibliootus.entity.Author.getAuthor;
import static ru.otus.training.alekseimorozov.bibliootus.entity.Genre.getGenre;

@Repository
public class BookDaoJdbc implements BookDao {
    private static final String EAGER_READ_ALL = "WITH author_to_book AS (SELECT book_id, author_id, full_name " +
            "FROM author_to_book_map m JOIN authors a ON m.author_id = a.id) " +
            "SELECT b.id AS id, title, genre_id, name, author_id, full_name " +
            "FROM books b JOIN genres g ON g.id = b.genre_id " +
            "LEFT JOIN author_to_book ab ON ab.book_id = b.id";
    private static final String CREATE_BOOK = "INSERT INTO BOOKS (TITLE, GENRE_ID) VALUES (:title, :genreId)";
    private static final String ADD_AUTHOR = "INSERT INTO AUTHOR_TO_BOOK_MAP (AUTHOR_ID, BOOK_ID)" +
            "VALUES(:authorId, :bookId)";
    private static final String READ_BY_ID = EAGER_READ_ALL + " WHERE b.id = :id";
    private static final String SELECT_BY_NAME = EAGER_READ_ALL + "   WHERE UPPER(b.title) LIKE '%'||:title||'%'";
    private static final String SELECT_BY_AUTHOR_NAME = EAGER_READ_ALL + " WHERE b.id IN " +
            "(SELECT BOOK_ID FROM AUTHOR_TO_BOOK_MAP WHERE AUTHOR_ID IN " +
            "(SELECT A.ID FROM AUTHORS A WHERE UPPER(A.FULL_NAME) LIKE '%'||:name||'%'))";
    private static final String SELECT_BY_AUTHOR_ID = EAGER_READ_ALL + " WHERE b.id IN " +
            "(SELECT BOOK_ID FROM AUTHOR_TO_BOOK_MAP WHERE AUTHOR_ID = :id)";
    private static final String SELECT_BY_GENRE_ID = EAGER_READ_ALL + " WHERE g.id = :id";
    private static final String UPDATE_NAME = "UPDATE BOOKS SET title = :title WHERE id = :id";
    private static final String UPDATE_GENRE = "UPDATE BOOKS SET genre_id = :genreId WHERE id = :bookId";
    private static final String DELETE_AUTHOR = "DELETE FROM AUTHOR_TO_BOOK_MAP WHERE AUTHOR_ID = :authorId AND " +
            "BOOK_ID = :bookId";
    private static final String DELETE_AUTHOR_TO_BOOK_MAPPING = "DELETE FROM AUTHOR_TO_BOOK_MAP WHERE BOOK_ID = :bookId";
    private static final String DELETE_BOOK = "DELETE FROM BOOKS WHERE ID = :bookId";

    private NamedParameterJdbcOperations jdbc;
    private ResultSetExtractor<Book> bookExtractor;
    private ResultSetExtractor<List<Book>> eagerBooksExtractor;

    @Autowired
    public BookDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
        bookExtractor = (resultSet) -> {
            Book book = null;
            while (resultSet.next()) {
                if (book == null) {
                    book = new Book();
                    book.setId(resultSet.getLong("id"));
                    book.setTitle(resultSet.getString("title"));
                    book.setGenre(getGenre(resultSet.getLong("genre_id"), resultSet.getString("name")));
                }
                book.getAuthors().add(getAuthor(resultSet.getLong("author_id"), resultSet.getString("full_name")));
            }
            return book == null ? new Book() : book;
        };
        eagerBooksExtractor = (resultSet) -> {
            Map<Long, Book> books = new HashMap<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Book book = books.get(id);
                if (book == null) {
                    book = new Book();
                    book.setId(id);
                    book.setTitle(resultSet.getString("title"));
                    book.setGenre(getGenre(resultSet.getLong("genre_id"), resultSet.getString("name")));
                    books.put(id, book);
                }
                book.getAuthors().add(getAuthor(resultSet.getLong("author_id"), resultSet.getString("full_name")));
            }
            return new ArrayList<>(books.values());
        };
    }

    @Override
    public Book create(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource().
                addValue("title", book.getTitle()).
                addValue("genreId", book.getGenre().getId());
        jdbc.update(CREATE_BOOK, params, keyHolder);
        book.setId(keyHolder.getKey().longValue());
        List<SqlParameterSource> batchParams = new ArrayList<>();
        for (Author author : book.getAuthors()) {
            batchParams.add(new MapSqlParameterSource().addValue("authorId", author.getId()).
                    addValue("bookId", book.getId()));
        }
        jdbc.batchUpdate(ADD_AUTHOR, batchParams.toArray(new SqlParameterSource[]{}));
        return book;
    }

    @Override
    public List<Book> readAll() {
        return jdbc.query(EAGER_READ_ALL, eagerBooksExtractor);
    }

    @Override
    public Book readById(Long id) {
        return jdbc.query(READ_BY_ID, new MapSqlParameterSource().addValue("id", id), bookExtractor);
    }

    @Override
    public List<Book> findByName(String name) {
        return jdbc.query(SELECT_BY_NAME, new MapSqlParameterSource().addValue("title", name.toUpperCase()),
                eagerBooksExtractor);
    }

    @Override
    public List<Book> findByAuthorName(String name) {
        return jdbc.query(SELECT_BY_AUTHOR_NAME, new MapSqlParameterSource().addValue("name", name.toUpperCase()),
                eagerBooksExtractor);
    }

    @Override
    public List<Book> findByAuthorId(Long id) {
        return jdbc.query(SELECT_BY_AUTHOR_ID, new MapSqlParameterSource().addValue("id", id), eagerBooksExtractor);
    }

    @Override
    public List<Book> findByGenreId(Long id) {
        return jdbc.query(SELECT_BY_GENRE_ID, new MapSqlParameterSource().addValue("id", id), eagerBooksExtractor);
    }

    @Override
    public void updateBookName(Long id, String title) {
        jdbc.update(UPDATE_NAME, new MapSqlParameterSource().addValue("id", id).addValue("title", title));
    }

    @Override
    public void updateBookGenre(Long bookId, Long genreId) {
        jdbc.update(UPDATE_GENRE, new MapSqlParameterSource().addValue("bookId", bookId).addValue("genreId", genreId));
    }

    @Override
    public void addAuthorToBook(Long bookId, Long authorId) {
        jdbc.update(ADD_AUTHOR, new MapSqlParameterSource().addValue("bookId", bookId).addValue("authorId", authorId));
    }

    @Override
    public void removeAuthorFromBook(Long bookId, Long authorId) {
        jdbc.update(DELETE_AUTHOR, new MapSqlParameterSource().addValue("bookId", bookId).
                addValue("authorId", authorId));
    }

    @Override
    public void delete(Long id) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("bookId", id);
        jdbc.update(DELETE_AUTHOR_TO_BOOK_MAPPING, params);
        jdbc.update(DELETE_BOOK, params);
    }
}