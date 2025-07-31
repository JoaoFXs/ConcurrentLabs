package io.github.jfelixy.concurrentlabs.repository;


import io.github.jfelixy.concurrentlabs.domain.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

}
