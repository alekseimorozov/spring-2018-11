package ru.otus.training.alekseimorozov.practicelib.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.training.alekseimorozov.practicelib.entity.Genre;

import java.util.List;

@Repository("genreDao")
public class GenreDaoJdbc extends AbstractLibraryDaoJdbc<Genre> implements CommonLibraryDao<Genre> {
    private static final String CREATE_SQL = "INSERT INTO GENRES (NAME) VALUES(?)";

    public GenreDaoJdbc(NamedParameterJdbcOperations jdbc) {
        super(jdbc);
    }

    @Override
    public void create(Genre genre) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbc().update(CREATE_SQL, getSqlParameterSource(genre), keyHolder);
        genre.setId((Long) keyHolder.getKey());
    }

    @Override
    public Genre readById(Long id) {
        return null;
    }

    @Override
    public List<Genre> readAll() {
        return null;
    }

    @Override
    public int update(Genre entity) {
        return 0;
    }

    @Override
    public int delete(Genre entity) {
        return 0;
    }
}
