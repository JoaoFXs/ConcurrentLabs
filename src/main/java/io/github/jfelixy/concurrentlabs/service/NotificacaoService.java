package io.github.jfelixy.concurrentlabs.service;

import io.github.jfelixy.concurrentlabs.domain.model.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class NotificacaoService {

    private final JavaMailSender mailSender;

    @Autowired
    public NotificacaoService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarConfirmacao(Reserva reserva){
        SimpleMailMessage message = new SimpleMailMessage();
        /** Seta destinatario **/
        message.setTo(reserva.getProfessor().getEmail());
        /** Seta assunto **/
        message.setSubject("Confirmação de Reserva - Laboratório " + reserva.getLaboratorio().getNome());
        /** Seta texto **/
        message.setText(gerarCorpoEmail(reserva));
        mailSender.send(message);
    }

    private String gerarCorpoEmail(Reserva reserva) {
        return String.format(
                "Olá, Professor(a) %s!\n\n" +
                        "Sua reserva foi confirmada:\n\n" +
                        "Laboratório: %s\n" +
                        "Data/Horário: %s\n" +
                        "Computadores reservados: %d\n\n" +
                        "Atenciosamente,\nEquipe de Reservas da Universidade",
                reserva.getProfessor().getNome(),
                reserva.getLaboratorio().getNome(),
                reserva.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                reserva.getLaboratorio().getCapacidadeComputadores()
        );
    }

}
