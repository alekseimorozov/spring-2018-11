package ru.otus.training.alekseimorozov.todolist.taskentities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataMongoTest
public class ToDoTaskEntitiesTest {
    @Autowired
    private MongoTemplate template;

    @Test
    @DisplayName("entity ToDoTask is correct saved to and read from the MongoDB databese")
    public void TaskEntityTest() {
        ToDoTask testTask = ToDoTask.builder()
                .title("test task")
                .description("test task to test mongoDB mapping")
                .status(Status.NEW)
                .progress(0)
                .build();
        template.save(testTask);
        assertThat(template.findById(testTask.getId(), ToDoTask.class)).isEqualTo(testTask);
    }
}