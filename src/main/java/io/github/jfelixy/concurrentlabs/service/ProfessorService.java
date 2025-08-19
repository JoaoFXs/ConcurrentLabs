package io.github.jfelixy.concurrentlabs.service;

import io.github.jfelixy.concurrentlabs.domain.model.Professor;
import io.github.jfelixy.concurrentlabs.dto.request.ProfessorRequest;
import io.github.jfelixy.concurrentlabs.exceptions.TeacherAlreadyEnrolledException;
import io.github.jfelixy.concurrentlabs.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Transactional
    public Professor criarProfessor(ProfessorRequest professor){

        if(professorRepository.findByMatricula(professor.matricula()) != null){
            throw new TeacherAlreadyEnrolledException(String.format("Professor de matricula %s já está cadastrado.",professor.matricula()));
        }
        return professorRepository.save(new Professor(professor.nome(), professor.email(), professor.matricula()));
    }
}
