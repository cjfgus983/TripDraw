package HyeonRi.dreamTravelApp.controller;

import HyeonRi.dreamTravelApp.dto.User;
import HyeonRi.dreamTravelApp.repository.UserRepository;
import HyeonRi.dreamTravelApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController // REST API 컨트롤러
@RequestMapping("/api") // 모든 URL은 /api/로 시작
public class SignupController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // 1단계: 이메일로 인증 코드 요청 /api/send-code 로 요청이 오면 아래 메서드가 실행
    @PostMapping("/send-code")
    public ResponseEntity<?> sendEmail(@RequestBody Map<String, String> body) {
        String email = body.get("email");

        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 가입된 이메일입니다.");
        }

        emailService.sendVerificationCode(email);
        return ResponseEntity.ok("인증코드 발송");
    }

    // 2단계: 인증 코드 확인 후 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String code = body.get("code");

        // 인증 코드 일치 확인
        if (!emailService.verifyCode(email, code)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 코드가 올바르지 않습니다.");
        }

        // 사용자 생성 및 저장
        User user = new User();
        user.setEmail(email);
        user.setPassword(password); // 실무에선 BCrypt 암호화 필요
        user.setEmailVerified(true);
        userRepository.save(user);

        return ResponseEntity.ok("회원가입 완료");
    }
}
