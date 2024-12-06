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
       // String subject = "Email verification";
        //String body ="your verification otp is: "+otp;

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom("prueba@gmail.com","soporte");
        mimeMessageHelper.setTo(email);
        String subject = "Aquí está su contraseña única (OTP): ¡caduca en 5 minutos!";
        String content = "<p>Hola " + "</p>"
                + "<p>Por razones de seguridad, debe utilizar la siguiente  "
                + "Contraseña de un solo uso para iniciar sesión:</p>"
                + "<p><b>" + otp + "</b></p>"
                + "<br>"
                + "<p>Nota: esta OTP expirará en 5 minutos.</p>";


        //mimeMessageHelper.setSubject("Verify OTP");
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content,true);
        /**mimeMessageHelper.setText("""
        <div>
          <a href="http://localhost:8082/verify-account?email=%s&otp=%s" target="_blank">click link to verify</a>
        </div>
        """.formatted(email, otp), true);**/

        javaMailSender.send(mimeMessage);
    }

}
