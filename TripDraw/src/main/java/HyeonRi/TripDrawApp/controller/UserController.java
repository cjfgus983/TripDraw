package HyeonRi.TripDrawApp.controller;

import HyeonRi.TripDrawApp.domain.LoginType;
import HyeonRi.TripDrawApp.domain.User;
import HyeonRi.TripDrawApp.dto.EmailVerifyRequestDto;
import HyeonRi.TripDrawApp.dto.UpdateProfileRequest;
import HyeonRi.TripDrawApp.dto.UserProfileDto;
import HyeonRi.TripDrawApp.dto.UserRegisterRequestDto;
import HyeonRi.TripDrawApp.dto.findEmail.FindEmailRequestDto;
import HyeonRi.TripDrawApp.dto.findEmail.FindEmailResponseDto;
import HyeonRi.TripDrawApp.dto.findEmail.FindEmailVerifyRequestDto;
import HyeonRi.TripDrawApp.dto.findPassword.FindPasswordRequestDto;
import HyeonRi.TripDrawApp.security.CustomUserDetails;
import HyeonRi.TripDrawApp.service.EmailService;
import HyeonRi.TripDrawApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody UserRegisterRequestDto request) {
        userService.register(request);
        return ResponseEntity.ok("회원가입 완료");
    }

    /** (1) 이메일 인증코드 발송 */
    @PostMapping("/email/send")
    public ResponseEntity<Void> sendEmailCode(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        emailService.sendVerificationCode(email);
        return ResponseEntity.ok().build();
    }
    /** (2) 이메일 인증코드 검증 */
    @PostMapping("/email/verify")
    public ResponseEntity<Map<String, Boolean>> verifyEmailCode(
            @RequestBody EmailVerifyRequestDto dto
    ) {
        boolean ok = emailService.verifyCode(dto.getEmail(), dto.getCode());
        return ResponseEntity.ok(Map.of("verified", ok));
    }

    /**
    *경로: /api/users/check-email
    파라미터: ?email=test@example.com
    응답:
    이메일이 중복되지 않으면 {"available":true}
    중복이면 {"available":false}

     이메일 중복 체크하는 부분
    * */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean available = userService.isEmailAvailable(email);
        // { "available": true } 또는 { "available": false }
        return ResponseEntity.ok(Map.of("available", available));
    }

    // 토큰에서 이메일을 이용해 사용자의 정보를 알아내기 위함 (비밀번호 제외)
    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getProfile(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        String email     = principal.getUsername();
        LoginType type   = principal.getLoginType();

        System.out.println(type);

        // 혹은 DB에서 다시 정확히 조회하고 싶다면
        User u = userService.findByEmailAndLoginType(email, type);

        UserProfileDto dto = new UserProfileDto(
                u.getUserId(), u.getEmail(), u.getName(),
                u.getPhoneNumber(), u.getAddress(),
                u.getAddressDetail(), u.getNickname(),
                u.getRole(), u.getLoginType()
        );
        return ResponseEntity.ok(dto);
    }




    // (3-1) 아이디 찾기용 인증 코드 발송
    @PostMapping("/find-email/send")
    public ResponseEntity<Void> sendFindEmailByPhone(@RequestBody FindEmailRequestDto dto) {
        System.out.println(dto.getPhoneNumber());
        System.out.println(dto.getEmail());

        Optional<String> emailOpt = userService.findEmailByNameAndPhoneNumber(
                dto.getName(),
                dto.getPhoneNumber());
        if (emailOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        emailService.sendVerificationCode(dto.getEmail());
        return ResponseEntity.ok().build();
    }

    // (3-2) 인증 코드 검증 후 아이디 반환
    @PostMapping("/find-email/verify")
    public ResponseEntity<FindEmailResponseDto> verifyFindId(
            @RequestBody FindEmailVerifyRequestDto dto
    ) {
        // 1) 코드 검증
        boolean codeOk = emailService.verifyCode(dto.getEmail(), dto.getCode());
        // 2) 이름·이메일 일치 재확인
        boolean match = userService.isNameEmailMatch(dto.getName(), dto.getEmail());

        if (!codeOk || !match) {
            return ResponseEntity
                    .badRequest()
                    .body(new FindEmailResponseDto("", false));
        }
        // 3) 성공 시 이메일(아이디) 반환
        return ResponseEntity.ok(new FindEmailResponseDto(dto.getEmail(), true));
    }


    /**
     * POST /api/users/find-password
     * { "email": "user@example.com" }
     * → 이메일이 DB에 존재하면 임시 비밀번호 발송(200 OK),
     *   아니면 400 Bad Request
     */
    @PostMapping("/find-password")
    public ResponseEntity<String> sendTempPassword(@RequestBody FindPasswordRequestDto dto) {
        if (!userService.isExistEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body("유효하지 않은 요청입니다: 이메일 형식이 잘못되었습니다");
        }
        userService.resetPasswordAndSendEmail(dto.getEmail());
        return ResponseEntity.ok("임시 비밀번호 발송 됐습니다.");
    }

    /**
    * 비밀번호 검증
     * 회원정보 수정할 때 필요함
    *
    * */
    @PostMapping("/verify-password")
    public ResponseEntity<String> verifyPassword(@RequestBody Map<String,String> body,
                                               Authentication authentication) {
        String rawPw = body.get("password");
        System.out.println(rawPw);

        String email = authentication.getName();
        System.out.println(email);

        if (!userService.checkPassword(email, rawPw)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 틀림");
        }
        return ResponseEntity.ok("비밀번호 인증 완료");
    }


    // 유저 정보를 수정하는 부분
    @PutMapping("/me")
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateProfileRequest request,
                                              Authentication authentication) {
        String email = authentication.getName();
        userService.updateProfile(email, request);
        return ResponseEntity.ok().build();
    }

    // 회원 탈퇴를 의뢰할 것
    @PostMapping("/delete-user")
    public ResponseEntity<String> DeleteUser(@RequestBody Map<String,String> body,
                                                 Authentication authentication) {
        String rawPw = body.get("password"); // 입력 받은 패스워드
        System.out.println(rawPw); // 한번 출력해보고

        String email = authentication.getName(); // 인증에서 이메일을 받아옴
        System.out.println(email); // 이메일도 일치하는가

        if (!userService.checkPassword(email, rawPw)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 틀림");
        }

        userService.deleteUser(email);

        return ResponseEntity.ok("회원 삭제 완료");
    }


}