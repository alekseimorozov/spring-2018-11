package ru.otus.training.alekseimorozov.todolist.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.training.alekseimorozov.todolist.taskentities.ToDoTask;

@Repository
public interface TaskRepository extends MongoRepository<ToDoTask, String> {
}
