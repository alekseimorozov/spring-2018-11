package ru.otus.training.alekseimorozov.todolist.contollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.training.alekseimorozov.todolist.repo.TaskRepository;
import ru.otus.training.alekseimorozov.todolist.taskentities.Status;
import ru.otus.training.alekseimorozov.todolist.taskentities.ToDoTask;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {
    private TaskRepository taskRepository;

    @Autowired
    public TaskRestController(TaskRepository repository) {
        this.taskRepository = repository;
    }

    @GetMapping()
    public Flux<ToDoTask> getAllTasks() {
        return taskRepository.findAll();
    }

    @GetMapping("/{taskId}")
    public Mono<ToDoTask> getById(@PathVariable String taskId) {
        return taskRepository.findById(taskId)
                .switchIfEmpty(Mono.just(ToDoTask.builder().title("New task").status(Status.NEW).build()));
    }

    @PostMapping()
    public Mono<ToDoTask> saveTask(ToDoTask task) {
        if (task.getId() != null && task.getId().isEmpty()) {
            task.setId(null);
        }
        return taskRepository.save(task);
    }

    @DeleteMapping("/{taskId}")
    public void delete(@PathVariable String taskId) {
        taskRepository.deleteById(taskId).subscribe();
    }
}