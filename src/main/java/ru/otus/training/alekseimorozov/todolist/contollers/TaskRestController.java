package ru.otus.training.alekseimorozov.todolist.contollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.training.alekseimorozov.todolist.repo.TaskRepository;
import ru.otus.training.alekseimorozov.todolist.taskentities.Status;
import ru.otus.training.alekseimorozov.todolist.taskentities.ToDoTask;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskRestController {
    private TaskRepository taskRepository;

    @Autowired
    public TaskRestController(TaskRepository repository) {
        this.taskRepository = repository;
    }

    @GetMapping("/all")
    public List<ToDoTask> getAllTasks() {
        return taskRepository.findAll();
    }

    @GetMapping("/one")
    public ToDoTask getById(@RequestParam(name = "taskId") String taskId, Model model) {
        return taskRepository.findById(taskId)
                .orElse(ToDoTask.builder().title("New task").status(Status.NEW).build());
    }

    @PostMapping("/save")
    public ToDoTask saveTask(ToDoTask task) {
        if (task.getId() != null && task.getId().isEmpty()) {
            task.setId(null);
        }
        return taskRepository.save(task);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam("taskId") String taskId) {
        taskRepository.deleteById(taskId);
    }
}