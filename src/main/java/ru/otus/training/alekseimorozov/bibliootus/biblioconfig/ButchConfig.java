package ru.otus.training.alekseimorozov.bibliootus.biblioconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.otus.training.alekseimorozov.bibliootus.entity.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EnableBatchProcessing
@Configuration
public class ButchConfig {
    private static final Logger logger = LoggerFactory.getLogger(ButchConfig.class);
    private static final String INSERT_AUTHOR = "INSERT INTO AUTHORS (full_name) VALUES(:fullName)";
    private static final String INSERT_GENRE = "INSERT INTO GENRES (name) VALUES(:name)";
    private static final String INSERT_BOOK = "INSERT INTO BOOKS (title, genre_id) VALUES(:title, (SELECT id " +
            "FROM GENRES WHERE name = :genre.name))";
    private static final String INSERT_AUTHOR_TO_BOOK = "INSERT INTO AUTHOR_TO_BOOK_MAP (author_id, book_id) VALUES" +
            "(:authorId, :bookId)";

    @Autowired
    private JobBuilderFactory jobBuilder;
    @Autowired
    private StepBuilderFactory stepBuilder;
    @Autowired
    private MongoTemplate template;
    @Autowired
    private DataSource dataSource;

    @Bean
    public ItemReader<Genre> readGenre() {
        return new MongoItemReaderBuilder<Genre>()
                .name("mongoGenreReader")
                .template(template)
                .collection("genres")
                .targetType(Genre.class)
                .jsonQuery("{}")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public ItemReader<Book> readBook() {
        return new MongoItemReaderBuilder<Book>()
                .name("mongoBookReader")
                .template(template)
                .collection("books")
                .targetType(Book.class)
                .jsonQuery("{}")
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Genre> writeGenre() {
        return new JdbcBatchItemWriterBuilder<Genre>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .dataSource(dataSource)
                .sql(INSERT_GENRE)
                .build();
    }

    @Bean
    public ItemWriter<Book> customBookWriter() {
        return items -> {
            NamedParameterJdbcOperations template = new NamedParameterJdbcTemplate(dataSource);
            KeyHolder bookIdHolder = new GeneratedKeyHolder();
            KeyHolder authorIdHolder = new GeneratedKeyHolder();
            items.stream()
                    .flatMap(book -> {
                        template.update(INSERT_BOOK, new BeanPropertySqlParameterSource(book), bookIdHolder);
                        Long bookId = bookIdHolder.getKey().longValue();
                        List<AuthorToBookRelation> authorToBookRelations = new ArrayList<>();
                        for (Author author : book.getAuthors()) {
                            template.update(
                                    INSERT_AUTHOR,
                                    new BeanPropertySqlParameterSource(author),
                                    authorIdHolder
                            );
                            Long authorId = authorIdHolder.getKey().longValue();
                            AuthorToBookRelation relation = new AuthorToBookRelation();
                            relation.setBookId(bookId);
                            relation.setAuthorId(authorId);
                            authorToBookRelations.add(relation);
                        }
                        return authorToBookRelations.stream();
                    })
                    .forEach(relation -> template.update(INSERT_AUTHOR_TO_BOOK, new BeanPropertySqlParameterSource(relation)));
        };
    }

    @Bean
    public Step genreStep(ItemReader<Genre> reader, ItemWriter<Genre> writer) {
        return stepBuilder.get("genreStep")
                .<Genre, Genre>chunk(5)
                .reader(reader)
                .writer(writer)
                .faultTolerant()
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step bookStep(ItemReader<Book> reader, ItemWriter<Book> writer) {
        return stepBuilder.get("bookStep")
                .<Book, Book>chunk(5)
                .reader(reader)
                .writer(writer)
                .faultTolerant()
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Job importLibrary(Step genreStep, Step bookStep) {
        return jobBuilder.get("importLibrary")
                .incrementer(new RunIdIncrementer())
                .start(genreStep)
                .next(bookStep)
                .build();
    }
}