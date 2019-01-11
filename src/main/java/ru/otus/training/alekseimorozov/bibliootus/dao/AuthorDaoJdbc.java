package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;

import java.util.List;

import static ru.otus.training.alekseimorozov.bibliootus.entity.Author.getAuthor;

@Repository
public class AuthorDaoJdbc implements AuthorDao {
    private static final String CREATE_SQL = "INSERT INTO authors (full_name) values(:fullName)";
    private static final String SELECT_ALL = "SELECT * FROM AUTHORS";
    private static final String SELECT_BY_ID = "SELECT * FROM AUTHORS WHERE ID = :id";
    private static final String SELECT_BY_NAME = "SELECT * FROM AUTHORS WHERE UPPER(full_name) LIKE '%'||:fullName||'%'";
    private static final String SELECT_BY_BOOK_ID = "SELECT A.ID, A.FULL_NAME FROM AUTHORS A " +
            "JOIN AUTHOR_TO_BOOK_MAP M ON M.AUTHOR_ID = A.ID WHERE M.BOOK_ID = :bookId";
    private static final String UPDATE_SQL = "UPDATE AUTHORS SET full_name = :fullName WHERE id = :id";
    private static final String DELETE_AUTHOR_SQL = "DELETE FROM AUTHORS WHERE id = :id";

    private NamedParameterJdbcOperations jdbcOperations;
    private ResultSetExtractor<Author> authorExtractor;
    private RowMapper<Author> authorRowMapper = BeanPropertyRowMapper.newInstance(Author.class);

    @Autowired
    public AuthorDaoJdbc(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
        authorExtractor = resultSet -> resultSet.next() ?
                getAuthor(resultSet.getLong("id"), resultSet.getString("full_name")) : new Author();
    }

    @Override
    public void create(Author author) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(CREATE_SQL, new BeanPropertySqlParameterSource(author), keyHolder);
        author.setId(keyHolder.getKey().longValue());
    }

    @Override
    public List<Author> readAll() {
        return jdbcOperations.query(SELECT_ALL, authorRowMapper);
    }

    @Override
    public Author findById(Long id) {
        return jdbcOperations.query(SELECT_BY_ID, new MapSqlParameterSource().addValue("id", id), authorExtractor);
    }

    @Override
    public List<Author> findByName(String fullName) {
        return jdbcOperations.query(SELECT_BY_NAME,
                new MapSqlParameterSource().addValue("fullName", fullName.toUpperCase()), authorRowMapper);
    }

    @Override
    public List<Author> findByBookId(Long bookId) {
        return jdbcOperations.query(SELECT_BY_BOOK_ID, new MapSqlParameterSource().addValue("bookId", bookId), authorRowMapper);
    }

    @Override
    public void update(Long id, String fullName) {
        jdbcOperations.update(UPDATE_SQL, new MapSqlParameterSource().addValue("id", id).
                addValue("fullName", fullName));
    }

    @Override
    public void delete(Long id) {
        jdbcOperations.update(DELETE_AUTHOR_SQL, new MapSqlParameterSource().addValue("id", id));
    }
}