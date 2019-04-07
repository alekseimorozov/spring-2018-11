package ru.otus.training.alekseimorozov.todolist.taskentities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class UserEntityTest {
    @Autowired
    private MongoTemplate template;

    @Test
    @DisplayName("User can be saved and read to/from database")
    public void userEntityTest() {
        User user = User.builder()
                .username("user")
                .password("password")
                .build();
        template.save(user);
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is("user"));
        assertThat(template.findOne(query, User.class)).isEqualTo(user);
    }
}