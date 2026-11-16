package com.almabridge.repository;

import com.almabridge.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstitutionRepository
        extends JpaRepository<Institution, Long> {

    Optional<Institution> findByInstitutionCode(String code);

    boolean existsByInstitutionCode(String code);
}