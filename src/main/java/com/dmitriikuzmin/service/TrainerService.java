package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.Trainer;

import java.util.List;

public interface TrainerService {
    Trainer add(Trainer trainer);

    List<Trainer> getAll();

    Trainer get(long id);

    Trainer update(Trainer trainer);

    Trainer delete(long id);
}
