package ru.otus.training.alekseimorozov.todolist.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.training.alekseimorozov.todolist.contollers.TaskRestController;
import ru.otus.training.alekseimorozov.todolist.repo.TaskRepository;
import ru.otus.training.alekseimorozov.todolist.taskentities.Status;
import ru.otus.training.alekseimorozov.todolist.taskentities.ToDoTask;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user@otus.ru")
public class TaskRestControllerTest {
    @Autowired
    private ApplicationContext context;
    @MockBean
    private TaskRepository repo;
    private WebTestClient client;

    @BeforeEach
    public void init() {
        client = WebTestClient.bindToController(new TaskRestController(repo))
                .configureClient()
                .baseUrl("/api/tasks")
                .build();
    }

    @Test
    public void getAllTasksTest() throws Exception {
        ToDoTask task = ToDoTask.builder()
                .id("abc")
                .title("test")
                .description("description")
                .progress(50)
                .status(Status.INPROGRESS)
                .build();

        when(repo.findAll()).thenReturn(Flux.just(task));

        client.get()
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].id").isEqualTo(task.getId())
                .jsonPath("$[0].title").isEqualTo(task.getTitle())
                .jsonPath("$[0].description").isEqualTo(task.getDescription())
                .jsonPath("$[0].progress").isEqualTo(task.getProgress())
                .jsonPath("$[0].status").isEqualTo(task.getStatus().toString());

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
        when(repo.findById(task.getId())).thenReturn(Mono.just(task));

        client.get().uri("/abc")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(task.getId())
                .jsonPath("$.title").isEqualTo(task.getTitle())
                .jsonPath("$.description").isEqualTo(task.getDescription())
                .jsonPath("$.progress").isEqualTo(task.getProgress())
                .jsonPath("$.status").isEqualTo(task.getStatus().toString());

        verify(repo).findById("abc");
    }

    @Test
    @DisplayName("return new task template if requested id does not exists")
    public void getByIdTaskNotExistTest() throws Exception {
        ToDoTask task = ToDoTask.builder()
                .title("New task")
                .status(Status.NEW)
                .build();
        when(repo.findById("new")).thenReturn(Mono.just(task));

        client.get().uri("/new")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(task.getId())
                .jsonPath("$.title").isEqualTo(task.getTitle())
                .jsonPath("$.description").isEqualTo(task.getDescription())
                .jsonPath("$.progress").isEqualTo(task.getProgress())
                .jsonPath("$.status").isEqualTo(task.getStatus().toString());

        verify(repo).findById("new");
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
        when(repo.save(any(ToDoTask.class))).thenReturn(Mono.just(task));

        client.post()
                .uri(u -> u.queryParam("id", task.getId())
                .queryParam("title", task.getTitle())
                .queryParam("description", task.getDescription())
                .queryParam("status", task.getStatus().toString())
                .queryParam("progress", task.getProgress() + "")
                .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(task.getId())
                .jsonPath("$.title").isEqualTo(task.getTitle())
                .jsonPath("$.description").isEqualTo(task.getDescription())
                .jsonPath("$.progress").isEqualTo(task.getProgress())
                .jsonPath("$.status").isEqualTo(task.getStatus().toString());

        verify(repo).save(eq(task));
    }

    @Test
    public void DeleteTest() throws Exception {
        client.delete()
                .uri("/abc")
                .exchange()
                .expectStatus().isOk();

        verify(repo).deleteById("abc");
    }
}