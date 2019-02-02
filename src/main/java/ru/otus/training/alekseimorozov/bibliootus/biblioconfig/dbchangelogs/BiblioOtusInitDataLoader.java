package ru.otus.training.alekseimorozov.bibliootus.biblioconfig.dbchangelogs;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.training.alekseimorozov.bibliootus.entity.Author;
import ru.otus.training.alekseimorozov.bibliootus.entity.Book;
import ru.otus.training.alekseimorozov.bibliootus.entity.Genre;

import java.util.Arrays;

@ChangeLog
public class BiblioOtusInitDataLoader {

    @ChangeSet(order = "001", id = "add initial data to database", author = "Aleksei Morozov")
    public void fillLibrary(MongoTemplate mongoTemplate) {
        Author pushkin = mongoTemplate.save(new Author("ALEXANDR PUSKIN"));
        Author nosov = mongoTemplate.save(new Author("NIKOLAY NOSOV"));
        Author zhitkov = mongoTemplate.save(new Author("BORIS ZHITKOV"));

        Genre novel = mongoTemplate.save(new Genre("NOVEL"));
        Genre story = mongoTemplate.save(new Genre("SHORT STORY"));
        Genre fiction = mongoTemplate.save(new Genre("FICTION"));

        Book firstBook = new Book();
        firstBook.setTitle("Сaptain's daughter");
        firstBook.setGenre(novel);
        firstBook.getAuthors().add(pushkin);
        firstBook.setComments(Arrays.asList("I like it", "It's brilliant"));
        Book secondBook = new Book();
        secondBook.setTitle("Dunno on the moon");
        secondBook.setGenre(fiction);
        secondBook.getAuthors().add(nosov);
        secondBook.setComments(Arrays.asList("Very funny", "This is my favorite", "Best I'v read ever"));
        Book thirdBook = new Book();
        thirdBook.setTitle("Sea Stories");
        thirdBook.setGenre(story);
        thirdBook.getAuthors().add(zhitkov);
        thirdBook.setComments(Arrays.asList("Very interesting", "I'v reread these stories about one million times"));
        mongoTemplate.save(firstBook);
        mongoTemplate.save(secondBook);
        mongoTemplate.save(thirdBook);
    }
}