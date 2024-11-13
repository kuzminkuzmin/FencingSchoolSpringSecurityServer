package com.dmitriikuzmin.repository;

import com.dmitriikuzmin.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByLogin(String login);

    Optional<Admin> findByLoginAndPassword(String login, String password);
}
