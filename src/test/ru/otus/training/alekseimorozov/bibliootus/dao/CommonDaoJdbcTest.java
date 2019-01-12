package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

@JdbcTest
public class CommonDaoJdbcTest {
    @Autowired
    private NamedParameterJdbcOperations jdbc;

    public NamedParameterJdbcOperations getJdbc() {
        return jdbc;
    }
}