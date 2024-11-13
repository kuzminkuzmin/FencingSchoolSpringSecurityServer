package com.dmitriikuzmin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @NotBlank(message = "Ведите логин")
    @Column(unique = true)
    private String login;

    @NonNull
    @NotBlank(message = "Ведите фамильию")
    private String surname;

    @NonNull
    @NotBlank(message = "Ведите имя")
    private String name;

    private String patronymic;

    @NonNull
    @NotBlank(message = "Ведите пароль")
    private String password;

    @CreatedDate
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime regDate = LocalDateTime.now();

    public String getRole() {
        return this.getClass().getName().split("\\.")[3];
    }
}
