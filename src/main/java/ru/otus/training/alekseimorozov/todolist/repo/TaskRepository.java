package ru.otus.training.alekseimorozov.todolist.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.training.alekseimorozov.todolist.taskentities.ToDoTask;

@Repository
public interface TaskRepository extends ReactiveMongoRepository<ToDoTask, String> {}