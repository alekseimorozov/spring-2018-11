package ru.otus.training.alekseimorozov.todolist.taskentities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tasks")
public class ToDoTask implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String title;
    private String description;
    private Status status;
    private int progress;
}