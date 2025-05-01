package HyeonRi.TripDrawApp.controller;

import HyeonRi.TripDrawApp.dto.User;
import HyeonRi.TripDrawApp.repository.UserRepository;
import HyeonRi.TripDrawApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController // REST API 컨트롤러
@RequestMapping("/api") // 모든 URL은 /api/로 시작 --> 나중에 signup으로 바꿀까
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

    //  1.5단계: 이메일 인증 코드 확인용 API 추가
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String code = body.get("code");

        if (emailService.verifyCode(email, code)) {
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 코드가 일치하지 않습니다.");
        }
    }

    // 2단계: 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String code = body.get("code");
        String nickname = body.get("nickname");

        if (!emailService.verifyCode(email, code)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 코드가 올바르지 않습니다.");
        }

        // 닉네임 중복 확인도 백엔드에서 가능 (프론트에서도 확인하니 이건 선택사항)
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 가입된 이메일입니다.");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setEmailVerified(true);
        userRepository.save(user);

        return ResponseEntity.ok("회원가입 완료");
    }

    // 닉네임 중복 확인
    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        boolean exists = userRepository.existsByNickname(nickname);
        if (exists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용 중인 닉네임입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 닉네임입니다.");
        }
    }

    @GetMapping("/home")
    public String home() {
        return "✅ 로그인 성공! 여기는 /home 입니다.";
    }
}
