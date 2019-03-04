package ru.otus.training.alekseimorozov.todolist.controllers;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.training.alekseimorozov.todolist.contollers.TaskRestController;
import ru.otus.training.alekseimorozov.todolist.repo.TaskRepository;
import ru.otus.training.alekseimorozov.todolist.taskentities.Status;
import ru.otus.training.alekseimorozov.todolist.taskentities.ToDoTask;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.hamcrest.number.OrderingComparison.comparesEqualTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskRestController.class)
public class TaskRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TaskRepository repo;

    @Test
    public void getAllTasksTest() throws Exception {
        ToDoTask task = ToDoTask.builder()
                .id("abc")
                .title("test")
                .description("description")
                .progress(50)
                .status(Status.INPROGRESS)
                .build();
        when(repo.findAll()).thenReturn(Arrays.asList(task));

        mvc.perform(get("/api/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", equalToIgnoringCase(task.getId())))
                .andExpect(jsonPath("$[0].title", equalToIgnoringCase(task.getTitle())))
                .andExpect(jsonPath("$[0].description", equalToIgnoringCase(task.getDescription())))
                .andExpect(jsonPath("$[0].progress", comparesEqualTo(task.getProgress())))
                .andExpect(jsonPath("$[0].status", equalToIgnoringCase(task.getStatus().toString())));
        verify(repo).findAll();
    }

    @Test
    @DisplayName("return task if requested id is exists")
    public void getByIdTaskExistTest() throws Exception {
        ToDoTask task = ToDoTask.builder()
                .id("abc")
                .title("test")
                .description("description")
                .progress(50)
                .status(Status.INPROGRESS)
                .build();
        when(repo.findById(task.getId())).thenReturn(Optional.of(task));

        mvc.perform(get("/api/one?taskId=abc")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalToIgnoringCase(task.getId())))
                .andExpect(jsonPath("$.title", equalToIgnoringCase(task.getTitle())))
                .andExpect(jsonPath("$.description", equalToIgnoringCase(task.getDescription())))
                .andExpect(jsonPath("$.progress", comparesEqualTo(task.getProgress())))
                .andExpect(jsonPath("$.status", equalToIgnoringCase(task.getStatus().toString())));
        verify(repo).findById("abc");
    }

    @Test
    @DisplayName("return task if requested id is exists")
    public void getByIdTaskNotExistTest() throws Exception {
        ToDoTask task = ToDoTask.builder()
                .title("New task")
                .status(Status.NEW)
                .build();
        when(repo.findById(task.getId())).thenReturn(Optional.of(task));

        mvc.perform(get("/api/one?taskId=")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", "").isEmpty())
                .andExpect(jsonPath("$.title", equalToIgnoringCase(task.getTitle())))
                .andExpect(jsonPath("$.description", "").isEmpty())
                .andExpect(jsonPath("$.progress", comparesEqualTo(task.getProgress())))
                .andExpect(jsonPath("$.status", equalToIgnoringCase(task.getStatus().toString())));
        verify(repo).findById("");
    }

    @Test
    public void saveTaskTest() throws Exception {
        ToDoTask task = ToDoTask.builder()
                .id("abc")
                .title("test")
                .description("description")
                .progress(50)
                .status(Status.INPROGRESS)
                .build();
        when(repo.save(any(ToDoTask.class))).thenReturn(task);

        mvc.perform(post("/api/save")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .param("id", task.getId())
                .param("title", task.getTitle())
                .param("description", task.getDescription())
                .param("status", task.getStatus().toString())
                .param("progress", task.getProgress() + ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalToIgnoringCase("abc")))
                .andExpect(jsonPath("$.title", equalToIgnoringCase(task.getTitle())))
                .andExpect(jsonPath("$.description", equalToIgnoringCase("description")))
                .andExpect(jsonPath("$.progress", comparesEqualTo(task.getProgress())))
                .andExpect(jsonPath("$.status", equalToIgnoringCase(task.getStatus().toString())));
        verify(repo).save(eq(task));
    }

    @Test
    public void DeleteTest() throws Exception {
        ToDoTask task = ToDoTask.builder()
                .id("abc")
                .title("test")
                .description("description")
                .progress(50)
                .status(Status.INPROGRESS)
                .build();
        mvc.perform(delete("/api/delete?taskId=abc"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(repo).deleteById("abc");
    }
}