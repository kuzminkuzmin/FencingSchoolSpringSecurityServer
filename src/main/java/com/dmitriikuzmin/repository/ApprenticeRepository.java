package com.dmitriikuzmin.repository;

import com.dmitriikuzmin.model.Apprentice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprenticeRepository extends JpaRepository<Apprentice, Long> {
}
