package ru.otus.training.alekseimorozov.todolist.config.dbchangelogs;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.otus.training.alekseimorozov.todolist.taskentities.Authority;
import ru.otus.training.alekseimorozov.todolist.taskentities.Status;
import ru.otus.training.alekseimorozov.todolist.taskentities.ToDoTask;
import ru.otus.training.alekseimorozov.todolist.taskentities.User;

import java.util.HashSet;
import java.util.Set;

@ChangeLog
public class ToDoTaskDBChanges {
    @ChangeSet(order = "003", id = "addTasks", author = "Aleksei Morozov")
    public void addInitTasks(MongoTemplate template) {
        template.save(getInstance("Дерево", "Посадить дерево", Status.DONE, 100));
        template.save(getInstance("Дом", "Построить дом", Status.DICLINE, 0));
        template.save(getInstance("Квартира", "Приобрести квартиру", Status.INPROGRESS, 55));
        template.save(getInstance("Сын", "Вырастить сына", Status.DICLINE, 0));
        template.save(getInstance("Дочь Полина", "Вырастить дочь", Status.INPROGRESS, 85));
        template.save(getInstance("Дочь София", "Вырастить дочь", Status.INPROGRESS, 10));
        template.save(getInstance("Spring", "Освоить Spring", Status.INPROGRESS, 65));
        template.save(getInstance("Работа", "Получить работу мечты", Status.INPROGRESS, 25));
        template.save(getInstance("Отпуск", "Поехать к теплому морю", Status.PENDING, 0));
    }

    @ChangeSet(order = "008", id = "addRolesAndUsers", author = "Aleksei Morozov")
    public void addUser(MongoTemplate template) {
        Authority roleUser = template.save(
                Authority.builder()
                        .authority("ROLE_USER")
                        .build()
        );
        Authority roleAdmin = template.save(
                Authority.builder()
                        .authority("ROLE_ADMIN")
                        .build()
        );
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Set<Authority> adminAuthority = new HashSet<>();
        adminAuthority.add(roleAdmin);
        Set<Authority> userAuthority = new HashSet<>();
        userAuthority.add(roleUser);
        template.save(
                User.builder()
                        .username("user@otus.ru")
                        .password(encoder.encode("password"))
                        .authorities(userAuthority)
                        .build()
        );
        template.save(
                User.builder()
                        .username("admin@otus.ru")
                        .password(encoder.encode("password"))
                        .authorities(adminAuthority)
                        .build()
        );
    }

    private ToDoTask getInstance(String title, String description, Status status, int progress) {
        return ToDoTask.builder()
                .title(title)
                .description(description)
                .status(status)
                .progress(progress)
                .build();
    }
}