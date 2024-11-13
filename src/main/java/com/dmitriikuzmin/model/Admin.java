package com.dmitriikuzmin.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "admins")
public class Admin extends User {
    @NonNull
    @Column(unique = true)
    @Email(message = "Неправильный формат почты")
    @NotBlank(message = "Введите почту")
    private String email;

    @NonNull
    @PositiveOrZero(message = "Зароботная плата не может быть отрицательной, как бы ни хотелось работадателю")
    //TODO if no salary field it'll be 0.
    @NotNull(message = "Введите размер зароботной платы")
    private long salary;

    public Admin(long id, @NonNull String login, @NonNull String surname, @NonNull String name, String patronymic,
                 @NonNull String password, LocalDateTime regDate, @NonNull String email, @NonNull long salary) {
        super(id, login, surname, name, patronymic, password, regDate);
        this.email = email;
        this.salary = salary;
    }
}
