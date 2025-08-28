package io.github.jfelixy.concurrentlabs.repository;


import io.github.jfelixy.concurrentlabs.domain.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Professor findByMatricula(String matricula);
}
