package ru.otus.training.alekseimorozov.todolist.taskentities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Data
@Builder
@Document(collection = "authorities")
public class Authority implements GrantedAuthority {
    @Id
    private String id;
    @Indexed(unique = true)
    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}