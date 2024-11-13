package com.dmitriikuzmin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "trainer_schedules")
public class TrainerSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ToString.Exclude
    @JsonIgnore
    @OneToOne
    @NonNull
    @MapsId
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime mondayStart;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime mondayEnd;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime tuesdayStart;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime tuesdayEnd;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime wednesdayStart;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime wednesdayEnd;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime thursdayStart;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime thursdayEnd;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime fridayStart;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime fridayEnd;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime saturdayStart;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime saturdayEnd;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime sundayStart;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime sundayEnd;

    public TrainerSchedule(@NonNull Trainer trainer) {
        this.trainer = trainer;
    }

    public void set(String day, LocalTime start, LocalTime end) {
        try {
            this.getClass().getDeclaredField(day + "Start").set(this, start);
            this.getClass().getDeclaredField(day + "End").set(this, end);
        } catch (IllegalAccessException ignored) {
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Неправильный день");

        }
    }

    public LocalTime[] getDayTime(String day) {
        try {
            return new LocalTime[]{
                    (LocalTime) this.getClass().getDeclaredField(day + "Start").get(this),
                    (LocalTime) this.getClass().getDeclaredField(day + "End").get(this)
            };
        } catch (IllegalAccessException ignored) {
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Неправильный день");
        }
        return new LocalTime[]{null, null};
    }
}
