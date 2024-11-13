package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.Admin;
import com.dmitriikuzmin.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setAdminRepository(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Admin addAdmin(Admin admin) {
        try {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
            return adminRepository.save(admin);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Логин или пароль уже используются");
        }
    }

    @Override
    public Admin get(String login, String password) {
        return this.adminRepository.findByLoginAndPassword(login, password)
                .orElseThrow(() -> new IllegalArgumentException("Неправильный логин или пароль"));
    }

    /*@Override
    public Admin get(String login) {
        return this.adminRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Неправильный логин"));
    }*/

    @Override
    public Admin get(long id) {
        return this.adminRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Нет администратора с id: " + id));
    }

    @Override
    public List<Admin> getAllAdmins() {
        return this.adminRepository.findAll();
    }

    @Override
    public Admin updateAdmin(Admin admin) {
        Admin base = this.get(admin.getId());
        base.setLogin(admin.getLogin());
        base.setName(admin.getName());
        base.setSurname(admin.getSurname());
        base.setPatronymic(admin.getPatronymic());
        base.setEmail(admin.getEmail());
        base.setSalary(admin.getSalary());
        try {
            this.adminRepository.save(base);
            return base;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Логин или пароль уже используются");
        }
    }

    @Override
    public Admin deleteAdmin(long id) {
        Admin admin = this.get(id);
        try {
            this.adminRepository.delete(admin);
            return admin;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
