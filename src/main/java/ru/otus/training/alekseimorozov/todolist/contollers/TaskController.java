package ru.otus.training.alekseimorozov.todolist.contollers;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.training.alekseimorozov.todolist.taskentities.Status;

@Log4j2
@Controller
public class TaskController {

    @RequestMapping("/edit")
    public String getEditView(@RequestParam(name = "taskId") String taskId, Model model) {
        model.addAttribute("id", taskId);
        model.addAttribute("statuses", Status.ALL);
        return "edit";
    }
}