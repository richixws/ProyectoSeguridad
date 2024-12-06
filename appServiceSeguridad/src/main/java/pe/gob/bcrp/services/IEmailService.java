package pe.gob.bcrp.services;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface IEmailService {

    public void sendOtpEmail(String email, String otp) throws UnsupportedEncodingException, MessagingException;
}
