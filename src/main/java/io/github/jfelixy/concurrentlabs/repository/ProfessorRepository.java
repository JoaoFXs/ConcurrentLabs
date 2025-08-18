package io.github.jfelixy.concurrentlabs.repository;


import io.github.jfelixy.concurrentlabs.domain.model.Laboratorio;
import io.github.jfelixy.concurrentlabs.domain.model.Professor;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Professor findByMatricula(String matricula);
}
