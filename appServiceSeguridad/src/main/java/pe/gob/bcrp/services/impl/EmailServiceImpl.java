package pe.gob.bcrp.services.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.services.IEmailService;

import java.io.UnsupportedEncodingException;

@Log4j2
@Service
@AllArgsConstructor
public class EmailServiceImpl implements IEmailService {


    private JavaMailSender javaMailSender;


    @Override
    public void sendOtpEmail(String email, String otp)  throws UnsupportedEncodingException, MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("prueba@gmail.com","soporte");
        mimeMessageHelper.setTo(email);
        String subject = "Aquí está su contraseña única de acceso: ¡caduca en 3 minutos!";
        String content = "<p>Hola " + "</p>"
                + "<p>Por razones de seguridad, debe utilizar la siguiente  "
                + "Contraseña de un solo uso para iniciar sesión:</p>"
                + "<p><b>" + otp + "</b></p>"
                + "<br>"
                + "<p>Nota: este codigo expirará en 3 minutos.</p>";

        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content,true);

        javaMailSender.send(mimeMessage);
    }

}
