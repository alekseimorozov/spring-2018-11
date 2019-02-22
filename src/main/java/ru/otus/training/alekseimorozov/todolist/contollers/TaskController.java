package ru.otus.training.alekseimorozov.todolist.contollers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.training.alekseimorozov.todolist.repo.TaskRepository;
import ru.otus.training.alekseimorozov.todolist.taskentities.Status;
import ru.otus.training.alekseimorozov.todolist.taskentities.ToDoTask;

@Log4j2
@Controller
public class TaskController {
    private TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @RequestMapping(value = {"/", "/tasks"})
    public String getAllTasks(Model model) {
        model.addAttribute("tasks", taskRepository.findAll());
        return "tasks";
    }

    @RequestMapping("/edit")
    public String getAddView(@RequestParam(name = "taskId") String taskId, Model model) {
        model.addAttribute("task", taskRepository.findById(taskId)
                .orElse(ToDoTask.builder().title("New task").status(Status.DONE).build()));
        return "edit";
    }

    @RequestMapping("/save")
    public String saveTask(@ModelAttribute("task") ToDoTask task) {
        taskRepository.save(task);
        return "edit";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam("taskId") String taskId, Model model) {
        taskRepository.deleteById(taskId);
        return getAllTasks(model);
    }

    @ModelAttribute("statuses")
    public Status[] allStatuses() {
        return Status.ALL;
    }
}
