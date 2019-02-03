package ru.otus.training.alekseimorozov.bibliootus.dao;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

@DataMongoTest
public class CommonDaoMongoTest {
    protected static Author firstAuthor;
    protected static Author secondAuthor;
    protected static Author thirdAuthor;
    protected static Author fourthAuthor;
    protected static Author fifthAuthor;
    protected static Genre firstGenre;
    protected static Genre secondGenre;
    protected static Book firstBook;
    protected static Book secondBook;
    protected static Book thirdBook;

    @Autowired
    private MongoTemplate mongoTemplate;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    @AfterEach
    public void finish() {
        mongoTemplate.dropCollection(Book.class);
        mongoTemplate.dropCollection(Author.class);
        mongoTemplate.dropCollection(Genre.class);
    }
}