package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.Trainer;
import com.dmitriikuzmin.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {
    private TrainerRepository trainerRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setTrainerRepository(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Trainer add(Trainer trainer) {
        try {
            trainer.setPassword(passwordEncoder.encode(trainer.getPassword()));
            return this.trainerRepository.save(trainer);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Такой тренер уже существует");
        }
    }

    @Override
    public List<Trainer> getAll() {
        return this.trainerRepository.findAll();
    }

    @Override
    public Trainer get(long id) {
        return this.trainerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Нет тренера с id: " + id));
    }

    @Override
    public Trainer update(Trainer trainer) {
        Trainer base = this.get(trainer.getId());
        base.setName(trainer.getName());
        base.setSurname(trainer.getSurname());
        base.setPatronymic(trainer.getPatronymic());
        base.setEmail(trainer.getEmail());
        base.setExperience(trainer.getExperience());
        try {
            this.trainerRepository.save(base);
            return base;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Такой тренер уже существует");
        }
    }

    @Override
    public Trainer delete(long id) {
        Trainer trainer = this.get(id);
        try {
            this.trainerRepository.delete(trainer);
            return trainer;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
