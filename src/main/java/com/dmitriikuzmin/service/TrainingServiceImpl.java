package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.Apprentice;
import com.dmitriikuzmin.model.Trainer;
import com.dmitriikuzmin.model.TrainerSchedule;
import com.dmitriikuzmin.model.Training;
import com.dmitriikuzmin.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {
    private TrainingRepository trainingRepository;
    private TrainerService trainerService;
    private ApprenticeService apprenticeService;

    @Autowired
    public void setTrainingRepository(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Autowired
    public void setApprenticeService(ApprenticeService apprenticeService) {
        this.apprenticeService = apprenticeService;
    }


    @Override
    public Training add(long apprenticeId, long trainerId, int numberGym, LocalDate date, LocalTime timeStart) {
        Trainer trainer = this.trainerService.get(trainerId);
        TrainerSchedule trainerSchedule = trainer.getTrainerSchedule();
        if (trainerSchedule == null) {
            throw new IllegalArgumentException("Тренер " + trainer.getName() + " " + trainer.getSurname() + " не имеет расписания");
        }
        LocalTime[] workTime = trainerSchedule.getDayTime(date.getDayOfWeek().toString().toLowerCase());
        if (workTime[1] == null) {
            throw new IllegalArgumentException(trainer.getName() + " " + trainer.getSurname() + " не работает в этот день");
        } else if (workTime[1].isBefore(timeStart.plusMinutes(90L))) {
            throw new IllegalArgumentException("Окончание тренировки будет после конца рабочего дня тренера");
        } else if (maxApprentices(trainer, date, timeStart)) {
            throw new IllegalArgumentException("У тренера уже есть 3 ученика в это время");
        } else if (maxApprenticesInGym(date, timeStart, numberGym)) {
            throw new IllegalArgumentException("Слишком много учеников в зале " + numberGym + " одновременно");
        }
        Apprentice apprentice = this.apprenticeService.get(apprenticeId);
        try {
            Training training = new Training(numberGym, date, timeStart);
            training.setTrainer(trainer);
            training.setApprentice(apprentice);
            return this.trainingRepository.save(training);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("У ученика " + apprentice.getSurname() + " " + apprentice.getName() + " уже есть тренировка ы этот день");
        }
    }

    @Override
    public Training get(long id) {
        return this.trainingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Нет тренировки с id: " + id));
    }

    @Override
    public List<Training> getByApprentice(long apprenticeId) {
        return this.trainingRepository.getTrainingsByApprenticeId(apprenticeId);
    }

    @Override
    public List<Training> getByTrainer(long trainerId) {
        return this.trainingRepository.getTrainingsByTrainerId(trainerId);
    }

    @Override
    public Training delete(long id) {
        try {
            Training training = this.get(id);
            this.trainingRepository.delete(training);
            return training;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    //метод, который вернет список всех свободных времен для заданного тренера, заданного зала, дате проведения тренировки с интервалом 30 имн.
    @Override
    public List<LocalTime> getAvailableTime(long trainerId, int numberGym, LocalDate date) {
        Trainer trainer = this.trainerService.get(trainerId);
        TrainerSchedule trainerSchedule = trainer.getTrainerSchedule();
        if (trainerSchedule == null) {
            return new ArrayList<>();
        }
        LocalTime[] workTime = trainerSchedule.getDayTime(date.getDayOfWeek().toString().toLowerCase());
        if (workTime[1] == null) {
            return new ArrayList<>();
        }


        List<LocalTime> res = new ArrayList<>();
        LocalTime start = workTime[0];
        while (start.isBefore(workTime[1].minusMinutes(60L))) {
            if (!maxApprentices(trainer, date, start) && !maxApprenticesInGym(date, start, numberGym)) {
                res.add(start);
            }
            start = start.plusMinutes(30L);
        }
        return res;
    }


    public boolean isOverlappingByTime(LocalTime timeStart1, LocalTime timeStart2) {
        return timeStart1.isBefore(timeStart2.plusMinutes(90L))
                && timeStart2.isBefore(timeStart1.plusMinutes(90L));
    }


    public boolean maxApprentices(Trainer trainer, LocalDate newTrainingDate, LocalTime newTrainingStart) {
        return this.trainingRepository.getByDateAndTrainer(newTrainingDate, trainer).stream()
                .filter(x -> isOverlappingByTime(newTrainingStart, x.getTimeStart()))
                .count() >= 3;
    }


    public boolean maxApprenticesInGym(LocalDate newTrainingDate, LocalTime newTrainingTime, int numberGym) {
        return this.trainingRepository.getTrainingsByDateAndNumberGym(newTrainingDate, numberGym).stream()
                .filter(x -> isOverlappingByTime(newTrainingTime, x.getTimeStart()) && x.getNumberGym() == numberGym)
                .count() >= 10;
    }
}
