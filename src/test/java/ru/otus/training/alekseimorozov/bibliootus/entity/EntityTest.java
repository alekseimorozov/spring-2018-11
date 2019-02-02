package ru.otus.training.alekseimorozov.bibliootus.entity;

import org.bson.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.mongodb.core.DbCallback;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@DataMongoTest
class EntityTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void authorTest() {
        Author expected = mongoTemplate.save(new Author( "Test"));
        assertThat(mongoTemplate.findOne(new Query(where("_id").is(expected.getId())), Author.class))
                .as("", expected);
    }

    @Test
    public void genreTest() {
        Genre expected = mongoTemplate.save(new Genre("Test"));
        assertThat(mongoTemplate.findOne(new Query(where("_id").is(expected.getId())), Genre.class))
                .as("", expected);
    }

    @Test
    public void bookTest() {
        Genre testGente = mongoTemplate.save(new Genre("Test genre"));
        Author first = mongoTemplate.save(new Author("First"));
        Author second = mongoTemplate.save(new Author("Second"));
        Author third = mongoTemplate.save(new Author("Third"));
        Book expected = new Book();
        expected.setTitle("Test");
        expected.setGenre(testGente);
        expected.getAuthors().add(first);
        expected.getAuthors().add(second);
        expected.getAuthors().add(third);
        mongoTemplate.save(expected);
        System.out.println("==============BEFORE================");
        System.out.println(mongoTemplate.findOne(new Query(where("_id").is(expected.getId())), Book.class));
        first.setFullName("Modified First");
        mongoTemplate.save(first);
        assertThat(mongoTemplate.findOne(new Query(where("_id").is(expected.getId())), Book.class))
                .as("", expected);
        System.out.println("==============AFTER AUTHOR FIRST WAS UPDATED================");
        System.out.println(mongoTemplate.findOne(new Query(where("_id").is(expected.getId())), Book.class));
        mongoTemplate.remove(first);
        System.out.println("==============AFTER AUTHOR FIRST WAS REMOVED================");
        System.out.println(mongoTemplate.findOne(new Query(where("_id").is(expected.getId())), Book.class));
        testGente.setName("UPDATED GENRE NAME");
        mongoTemplate.save(testGente);
        System.out.println("==============AFTER GENRE WAS UPDATED================");
        System.out.println(mongoTemplate.findOne(new Query(where("_id").is(expected.getId())), Book.class));

    }
}