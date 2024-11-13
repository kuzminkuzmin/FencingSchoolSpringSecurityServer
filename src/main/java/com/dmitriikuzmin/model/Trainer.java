package com.dmitriikuzmin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "trainers")
public class Trainer extends User {
    @NonNull
    @Column(unique = true)
    @Email(message = "Неправильный формат почты")
    @NotBlank(message = "Введите почту")
    private String email;

    @NonNull
    @PositiveOrZero(message = "Опыт не может быть отрицательным")
    //TODO if no salary field it'll be 0.
    @NotNull(message = "Введите опыт")
    private int experience;

    @ToString.Exclude
    @JsonIgnore
    @OneToOne(mappedBy = "trainer", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private TrainerSchedule trainerSchedule;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL)
    private List<Training> trainings;

    public Trainer(long id, @NonNull String login, @NonNull String surname, @NonNull String name, String patronymic,
                   @NonNull String password, LocalDateTime regDate, @NonNull String email) {
        super(id, login, surname, name, patronymic, password, regDate);
        this.email = email;
    }
}
