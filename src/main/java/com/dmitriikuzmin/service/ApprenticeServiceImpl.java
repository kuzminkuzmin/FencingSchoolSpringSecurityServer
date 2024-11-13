package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.Apprentice;
import com.dmitriikuzmin.repository.ApprenticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApprenticeServiceImpl implements ApprenticeService {
    private ApprenticeRepository apprenticeRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setApprenticeRepository(ApprenticeRepository apprenticeRepository) {
        this.apprenticeRepository = apprenticeRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Apprentice add(Apprentice apprentice) {
        try {
            apprentice.setPassword(passwordEncoder.encode(apprentice.getPassword()));
            return apprenticeRepository.save(apprentice);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Такой ученик уже существует");
        }
    }

    @Override
    public List<Apprentice> getAll() {
        return this.apprenticeRepository.findAll();
    }

    @Override
    public Apprentice get(long id) {
        return this.apprenticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Нет ученика с id:" + id));
    }

    @Override
    public Apprentice update(Apprentice apprentice) {
        Apprentice base = this.get(apprentice.getId());
        base.setName(apprentice.getName());
        base.setSurname(apprentice.getSurname());
        base.setPatronymic(apprentice.getPatronymic());
        base.setPhoneNumber(apprentice.getPhoneNumber());
        try {
            this.apprenticeRepository.save(base);
            return base;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Такой ученик уже существует");
        }
    }

    @Override
    public Apprentice delete(long id) {
        Apprentice apprentice = this.get(id);
        try {
            this.apprenticeRepository.delete(apprentice);
            return apprentice;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
