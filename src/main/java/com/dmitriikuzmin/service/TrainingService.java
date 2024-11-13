package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.Trainer;
import com.dmitriikuzmin.model.Training;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TrainingService {
    Training add(long apprenticeId, long trainerId, int numberGym, LocalDate date, LocalTime timeStart);

    Training get(long id);

    List<Training> getByApprentice(long apprenticeId);

    List<Training> getByTrainer(long trainerId);

    List<LocalTime> getAvailableTime(long trainerId, int numberGym, LocalDate date);

    Training delete(long id);
}
