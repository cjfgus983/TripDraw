package HyeonRi.TripDrawApp.service;
// 이메일 인증 코드 관리용

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, String> verificationCodes = new HashMap<>();

    // 이메일로 인증코드 전송
    public void sendVerificationCode(String toEmail) {
        String code = String.valueOf((int)(Math.random() * 900000) + 100000);
        verificationCodes.put(toEmail, code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("🌟 DreamTravelApp 이메일 인증 코드");
        message.setText("아래 인증 코드를 입력해주세요: \n\n" + code);
        message.setFrom("hyeonri13@gmail.com"); // 보내는 사람 (Gmail 주소)

        mailSender.send(message); // 실제 메일 발송
    }
    // 인증 코드 검증
    public boolean verifyCode(String email, String code) {
        return code.equals(verificationCodes.get(email));
    }
    // 임시 비밀번호 전송
    //sendSimpleMessage(email, subject, body);
    public void sendNewPassword(String email, String subject, String body)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("hyeonri13@gmail.com"); // 보내는 사람 (Gmail 주소)

        mailSender.send(message); // 실제 메일 발송
    }
}
