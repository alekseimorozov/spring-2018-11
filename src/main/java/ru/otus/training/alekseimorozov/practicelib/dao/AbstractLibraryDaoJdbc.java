package ru.otus.training.alekseimorozov.practicelib.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import ru.otus.training.alekseimorozov.practicelib.entity.CommonEntity;

public abstract class AbstractLibraryDaoJdbc<T extends CommonEntity> implements CommonLibraryDao<T> {
    private NamedParameterJdbcOperations jdbc;

    @Autowired
    public AbstractLibraryDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    public NamedParameterJdbcOperations getJdbc() {
        return jdbc;
    }

    protected SqlParameterSource getSqlParameterSource(T entity) {
        return new BeanPropertySqlParameterSource(entity);
    }
}