package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.TrainerSchedule;

import java.time.LocalTime;

public interface TrainerScheduleService {
    TrainerSchedule add(long trainerId, String day, LocalTime start, LocalTime end);

    TrainerSchedule get(long trainerId);

    TrainerSchedule delete(long trainerId, String day);
}
