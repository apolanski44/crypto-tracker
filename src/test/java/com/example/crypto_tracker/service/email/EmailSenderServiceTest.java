package com.example.crypto_tracker.service.email;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailSenderService emailSenderService;

    @Test
    void sendEmailSuccessfullySendsEmail() {
        String to = "dest@mail.com";
        String subject = "test";
        String text = "text test";

        emailSenderService.sendEmail(to, subject, text);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage email = captor.getValue();

        assertThat(email.getTo()).contains(to);
        assertThat(email.getSubject()).isEqualTo(subject);
        assertThat(email.getText()).isEqualTo(text);
    }
}
