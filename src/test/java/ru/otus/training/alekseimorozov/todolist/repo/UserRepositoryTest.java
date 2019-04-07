package ru.otus.training.alekseimorozov.todolist.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import reactor.test.StepVerifier;
import ru.otus.training.alekseimorozov.todolist.taskentities.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository repository;
    @Autowired
    private MongoTemplate template;

    @Test
    public  void findByUsernameTest() {
        User user = User.builder()
                .username("user")
                .password("password")
                .build();
        template.save(user);
        StepVerifier
                .create(repository.findByUsername(user.getUsername()))
                .assertNext(actual -> assertThat(actual).isEqualTo(user))
                .expectComplete()
                .verify();
    }
}