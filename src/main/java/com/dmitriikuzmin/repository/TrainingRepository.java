package com.dmitriikuzmin.repository;

import com.dmitriikuzmin.model.Trainer;
import com.dmitriikuzmin.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> getTrainingsByApprenticeId(long id);
    List<Training> getTrainingsByTrainerId(long id);
    List<Training> getByDateAndTrainer(LocalDate dare, Trainer trainer);
    List<Training> getTrainingsByDateAndNumberGym(LocalDate dare, int numberGym);
}
