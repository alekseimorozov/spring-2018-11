package ru.otus.training.alekseimorozov.bibliootus.biblioconfig;

import com.github.mongobee.Mongobee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoBeeConfig {
    @Bean
    public Mongobee mongobee(MongoTemplate template){
        return new Mongobee("mongodb://localhost:27017/bibliootus")
                .setMongoTemplate(template)
                .setDbName("bibliootus")
                .setChangeLogsScanPackage("ru.otus.training.alekseimorozov.bibliootus.biblioconfig.dbchangelogs");
    }
}