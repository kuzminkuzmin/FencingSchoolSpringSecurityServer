package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.Apprentice;

import java.util.List;

public interface ApprenticeService {
    Apprentice add(Apprentice apprentice);

    List<Apprentice> getAll();

    Apprentice get(long id);

    Apprentice update(Apprentice apprentice);

    Apprentice delete(long id);
}
