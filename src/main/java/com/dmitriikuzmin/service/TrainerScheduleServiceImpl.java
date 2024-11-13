package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.Trainer;
import com.dmitriikuzmin.model.TrainerSchedule;
import com.dmitriikuzmin.model.Training;
import com.dmitriikuzmin.repository.TrainerScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainerScheduleServiceImpl implements TrainerScheduleService {
    private TrainerScheduleRepository trainerScheduleRepository;
    private TrainerService trainerService;
    private TrainingService trainingService;

    @Autowired
    public void setTrainerScheduleRepository(TrainerScheduleRepository trainerScheduleRepository) {
        this.trainerScheduleRepository = trainerScheduleRepository;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Autowired
    public void setTrainingService(TrainingService trainingService) {
        this.trainingService = trainingService;
    }


    @Override
    public TrainerSchedule add(long trainerId, String day, LocalTime start, LocalTime end) {
        try {
            Trainer trainer = this.trainerService.get(trainerId);
            TrainerSchedule trainerSchedule = trainer.getTrainerSchedule();
            if (trainerSchedule == null) {
                trainerSchedule = new TrainerSchedule(trainer);
                trainer.setTrainerSchedule(trainerSchedule);
            }
            trainerSchedule.set(day, start, end);
            return trainerScheduleRepository.save(trainerSchedule);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public TrainerSchedule get(long trainerId) {
        try {
            Trainer trainer = this.trainerService.get(trainerId);
            if (trainer.getTrainerSchedule() == null) {
                throw new IllegalArgumentException("У тренера " + trainer.getSurname() + " " + trainer.getName() + " нет расписания");
            }
            return trainer.getTrainerSchedule();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public TrainerSchedule delete(long trainerId, String day) {
        try {
            Trainer trainer = this.trainerService.get(trainerId);
            if (trainer.getTrainerSchedule() == null) {
                throw new IllegalArgumentException("У тренера " + trainer.getSurname() + " " + trainer.getName() + " нет расписания");
            }
            TrainerSchedule trainerSchedule = trainer.getTrainerSchedule();
            trainerSchedule.set(day, null, null);
            this.trainerScheduleRepository.save(trainerSchedule);
            List<Training> trainings = this.trainingService.getByTrainer(trainerId)
                    .stream()
                    .filter(training -> training.getDate().getDayOfWeek().toString().toLowerCase().equals(day))
                    .collect(Collectors.toList());
            for (Training training : trainings) {
                this.trainingService.delete(training.getId());
            }
            return trainerSchedule;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
