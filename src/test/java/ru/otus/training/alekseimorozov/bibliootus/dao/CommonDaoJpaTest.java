package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommonDaoJpaTest {
    @Autowired
    private TestEntityManager entityManager;

    public TestEntityManager getEntityManager() {
        return entityManager;
    }
}