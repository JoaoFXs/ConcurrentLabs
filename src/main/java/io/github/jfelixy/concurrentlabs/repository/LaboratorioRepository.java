package io.github.jfelixy.concurrentlabs.repository;


import io.github.jfelixy.concurrentlabs.domain.model.Laboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaboratorioRepository extends JpaRepository<Laboratorio, Long> {

}
