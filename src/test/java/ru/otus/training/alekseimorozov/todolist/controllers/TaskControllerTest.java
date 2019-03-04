package ru.otus.training.alekseimorozov.todolist.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.training.alekseimorozov.todolist.taskentities.Status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("return edit view when request /edit url")
    public void getEditViewTest() throws Exception {
        mvc.perform(get("/edit?taskId=abcd"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("id","abcd"))
                .andExpect(model().attribute("statuses", Status.ALL))
                .andExpect(view().name("edit"));
    }
}