package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.User;
import com.dmitriikuzmin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User get(long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Нет пользователя с id: " + id));
    }

    @Override
    public User get(String login, String password) {
        return null;
    }

    @Override
    public User delete(long id) {
        try {
            User user = this.get(id);
            this.userRepository.delete(user);
            return user;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
