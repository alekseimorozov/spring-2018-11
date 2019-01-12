package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.List;

import static ru.otus.training.alekseimorozov.bibliootus.entity.Genre.getGenre;

@Repository
public class GenreDaoJdbc implements GenreDao {
    private static final String CREATE_SQL = "INSERT INTO GENRES (name) VALUES (:name)";
    private static final String SELECT_ALL = "SELECT * FROM GENRES";
    private static final String SELECT_BY_ID = "SELECT * FROM GENRES WHERE id = :id";
    private static final String UPDATE_SQL = "UPDATE GENRES SET name = :name WHERE id = :id";
    private static final String DELETE_SQL = "DELETE FROM GENRES WHERE id = :id";

    private NamedParameterJdbcOperations jdbc;
    private ResultSetExtractor<Genre> genreExtractor;
    private RowMapper<Genre> genreRowMapper = BeanPropertyRowMapper.newInstance(Genre.class);

    public GenreDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
        genreExtractor = (resultSet) -> resultSet.next() ?
                getGenre(resultSet.getLong("id"), resultSet.getString("name")) : new Genre();
    }

    @Override
    public Genre create(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(CREATE_SQL, new BeanPropertySqlParameterSource(genre), keyHolder);
        genre.setId(keyHolder.getKey().longValue());
        return genre;
    }

    @Override
    public List<Genre> readAll() {
        return jdbc.query(SELECT_ALL, genreRowMapper);
    }

    @Override
    public Genre readById(Long id) {
        return jdbc.query(SELECT_BY_ID, new MapSqlParameterSource().addValue("id", id), genreExtractor);
    }

    @Override
    public void update(Long genreId, String name) {
        jdbc.update(UPDATE_SQL, new BeanPropertySqlParameterSource(getGenre(genreId, name)));
    }

    @Override
    public void delete(Long id) {
        jdbc.update(DELETE_SQL, new MapSqlParameterSource().addValue("id", id));
    }
}