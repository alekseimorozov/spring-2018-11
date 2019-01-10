package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;

import java.util.List;

import static ru.otus.training.alekseimorozov.bibliootus.entity.Author.getAuthor;

@Repository
public class AuthorDaoJdbc implements AuthorDao {
    private static final String CREATE_SQL = "INSERT INTO authors (name) values(:name)";
    private static final String SELECT_ALL = "SELECT * FROM AUTHORS";
    private static final String SELECT_BY_ID = "SELECT * FROM AUTHORS WHERE ID = :id";
    private static final String SELECT_BY_NAME = "SELECT * FROM AUTHORS WHERE UPPER(name) LIKE '%'||:name||'%'";
    private static final String SELECT_BY_BOOK_ID = "SELECT A.ID, A.NAME FROM AUTHORS A " +
            "JOIN AUTHOR_TO_BOOK_MAP M ON M.AUTHOR_ID = A.ID " +
            "WHERE M.BOOK_ID = :bookId";
    private static final String UPDATE_SQL = "UPDATE AUTHORS SET name = :name WHERE id = :id";
    private static final String DELETE_AUTHOR_SQL = "DELETE FROM AUTHORS WHERE id = :authorId";

    private NamedParameterJdbcOperations jdbcOperations;
    private ResultSetExtractor<Author> authorExtractor;
    private RowMapper<Author> authorRowMapper;

    @Autowired
    public AuthorDaoJdbc(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
        authorRowMapper = (resultSet, i) -> getAuthor(resultSet.getLong("id"), resultSet.getString("name"));
        authorExtractor = resultSet -> resultSet.next() ?
                getAuthor(resultSet.getLong("id"), resultSet.getString("name")) : new Author();
    }

    @Override
    public void create(Author author) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(author);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(CREATE_SQL, params, keyHolder);
        author.setId(keyHolder.getKey().longValue());
    }

    @Override
    public List<Author> readAll() {
        return jdbcOperations.query(SELECT_ALL, authorRowMapper);
    }

    @Override
    public Author findById(Long id) {
        SqlParameterSource params  = new MapSqlParameterSource().addValue("id", id);
        return jdbcOperations.query(SELECT_BY_ID, params, authorExtractor);
    }

    @Override
    public List<Author> findByName(String name) {
        SqlParameterSource params  = new MapSqlParameterSource().addValue("name", name.toUpperCase());
        return jdbcOperations.query(SELECT_BY_NAME, params, authorRowMapper);
    }

    @Override
    public List<Author> findByBookId(Long bookId) {
        SqlParameterSource params  = new MapSqlParameterSource().addValue("bookId", bookId);
        return jdbcOperations.query(SELECT_BY_BOOK_ID, params, authorRowMapper);
    }

    @Override
    public void update(Long id, String name) {
        SqlParameterSource params  = new MapSqlParameterSource().addValue("id", id).addValue("name", name);
        jdbcOperations.update(UPDATE_SQL, params);
    }

    @Override
    public void delete(Long authorId) {
        SqlParameterSource params  = new MapSqlParameterSource().addValue("authorId", authorId);
        jdbcOperations.update(DELETE_AUTHOR_SQL, params);
    }
}