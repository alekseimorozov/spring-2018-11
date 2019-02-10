package ru.otus.training.alekseimorozov.todolist.config;

import com.github.mongobee.Mongobee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoBeeConfig {
    @Bean
    public Mongobee getMomgobee(MongoTemplate template) {
        return new Mongobee("mongodb://localhost:27017/todolist")
                .setMongoTemplate(template)
                .setDbName("todolist")
                .setChangeLogsScanPackage("ru.otus.training.alekseimorozov.todolist.config.dbchangelogs");
    }
}
