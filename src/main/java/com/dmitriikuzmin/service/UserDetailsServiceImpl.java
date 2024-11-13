package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.User;
import com.dmitriikuzmin.model.UserDetailsImpl;
import com.dmitriikuzmin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(login);
        user.orElseThrow(() -> new UsernameNotFoundException(login + " не найден"));
        return user.map(UserDetailsImpl::new).get();
    }
}
