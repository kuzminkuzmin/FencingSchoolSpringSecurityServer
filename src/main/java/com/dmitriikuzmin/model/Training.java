package com.dmitriikuzmin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "trainings", uniqueConstraints = {@UniqueConstraint(columnNames = {"apprentice_id", "date"})})
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private int numberGym;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "apprentice_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Apprentice apprentice;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate date;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime timeStart;
}
