package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;

import java.util.ArrayList;
import java.util.List;

import static ru.otus.training.alekseimorozov.bibliootus.entity.Genre.getGenre;

@Repository
public class BookDaoJdbc implements BookDao {
    private static final String READ_ALL = "SELECT b.id as id, b.name as name, g.id as genre_id, g.name as " +
            "genre_name FROM BOOKS b JOIN GENRES g ON g.id = b.genre_id ";
    private static final String CREATE_BOOK = "INSERT INTO BOOKS (NAME, GENRE_ID) VALUES (:name, :genreId)";
    private static final String ADD_AUTHOR = "INSERT INTO AUTHOR_TO_BOOK_MAP (AUTHOR_ID, BOOK_ID)" +
            "VALUES(:authorId, :bookId)";
    private static final String READ_BY_ID = READ_ALL + " WHERE b.id = :id";
    private static final String SELECT_BY_NAME = READ_ALL + " WHERE UPPER(b.name) LIKE '%'||:name||'%'";
    private static final String SELECT_BY_AUTHOR_NAME = READ_ALL + " WHERE b.id IN " +
            "(SELECT BOOK_ID FROM AUTHOR_TO_BOOK_MAP WHERE AUTHOR_ID IN " +
            "(SELECT A.ID FROM AUTHORS A WHERE UPPER(A.NAME) LIKE '%'||:name||'%'))";
    private static final String SELECT_BY_AUTHOR_ID = READ_ALL + " WHERE b.id IN " +
            "(SELECT BOOK_ID FROM AUTHOR_TO_BOOK_MAP WHERE AUTHOR_ID = :id)";
    private static final String SELECT_BY_GENRE_ID = READ_ALL + " WHERE g.id = :id";
    private static final String UPDATE_NAME = "UPDATE BOOKS SET NAME = :name WHERE id = :id";
    private static final String UPDATE_GENRE = "UPDATE BOOKS SET genre_id = :genreId WHERE id = :bookId";
    private static final String DELETE_AUTHOR = "DELETE FROM AUTHOR_TO_BOOK_MAP WHERE AUTHOR_ID = :authorId AND " +
            "BOOK_ID = :bookId";
    private static final String DELETE_AUTHOR_TO_BOOK_MAPPING = "DELETE FROM AUTHOR_TO_BOOK_MAP WHERE BOOK_ID = :bookId";
    private static final String DELETE_BOOK = "DELETE FROM BOOKS WHERE ID = :bookId";

    private NamedParameterJdbcOperations jdbc;
    private ResultSetExtractor<Book> bookResultSetExtractor;
    private RowMapper<Book> bookRowMapper;

    @Autowired
    public BookDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
        bookResultSetExtractor = (resultSet) -> {
            Book book = new Book();
            if (resultSet.next()) {
                book.setId(resultSet.getLong("id"));
                book.setName(resultSet.getString("name"));
                book.setGenre(getGenre(resultSet.getLong("genre_id"), resultSet.getString("genre_name")));
            }
            return book;
        };
        bookRowMapper = (resultSet, i) -> {
            Book book = new Book();
            book.setId(resultSet.getLong("id"));
            book.setName(resultSet.getString("name"));
            book.setGenre(getGenre(resultSet.getLong("genre_id"), resultSet.getString("genre_name")));
            return book;
        };
    }

    @Override
    public Book create(Book book) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource().
                addValue("name", book.getName()).
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
        return jdbc.query(READ_ALL, bookRowMapper);
    }

    @Override
    public Book readById(Long id) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        return jdbc.query(READ_BY_ID, params, bookResultSetExtractor);
    }

    @Override
    public List<Book> findByName(String name) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("name", name.toUpperCase());
        return jdbc.query(SELECT_BY_NAME, params, bookRowMapper);
    }

    @Override
    public List<Book> findByAuthorName(String name) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("name", name.toUpperCase());
        return jdbc.query(SELECT_BY_AUTHOR_NAME, params, bookRowMapper);
    }

    @Override
    public List<Book> findByAuthorId(Long id) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        return jdbc.query(SELECT_BY_AUTHOR_ID, params, bookRowMapper);
    }

    @Override
    public List<Book> findByGenreId(Long id) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        return jdbc.query(SELECT_BY_GENRE_ID, params, bookRowMapper);
    }

    @Override
    public void updateBookName(Long id, String name) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("id", id).
                addValue("name", name);
        jdbc.update(UPDATE_NAME, params);
    }

    @Override
    public void updateBookGenre(Long bookId, Long genreId) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("bookId", bookId).addValue("genreId", genreId);
        jdbc.update(UPDATE_GENRE, params);
    }

    @Override
    public void addAuthorToBook(Long bookId, Long authorId) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("bookId", bookId).
                addValue("authorId", authorId);
        jdbc.update(ADD_AUTHOR, params);
    }

    @Override
    public void removeAuthorFromBook(Long bookId, Long authorId) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("bookId", bookId).
                addValue("authorId", authorId);
        jdbc.update(DELETE_AUTHOR, params);
    }

    @Override
    public void delete(Long id) {
        SqlParameterSource params = new MapSqlParameterSource().addValue("bookId", id);
        jdbc.update(DELETE_AUTHOR_TO_BOOK_MAPPING, params);
        jdbc.update(DELETE_BOOK, params);
    }
}