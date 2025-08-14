package io.github.jfelixy.concurrentlabs.service;

import io.github.jfelixy.concurrentlabs.domain.model.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    private final JavaMailSender mailSender;

    @Autowired
    public NotificacaoService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailSimples(Reserva reserva){
        SimpleMailMessage message = new SimpleMailMessage();
        /** Seta destinatario **/
        message.setTo(reserva.getProfessor().getEmail());
        /** Seta assunto **/
        message.setSubject("Confirmação de Reserva - Laboratório " + reserva.getLaboratorio().getNome());
        /** Seta texto **/
        message.setText(gerarCorpoTexto());
        mailSender.send(message);
    }

}
