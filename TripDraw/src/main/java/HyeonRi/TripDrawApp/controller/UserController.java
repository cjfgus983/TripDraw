package HyeonRi.TripDrawApp.controller;

import HyeonRi.TripDrawApp.dto.UserRegisterRequest;
import HyeonRi.TripDrawApp.service.UserService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody UserRegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok("회원가입 완료");
    }

    /*
    *경로: /api/users/check-email
    파라미터: ?email=test@example.com
    응답:
    이메일이 중복되지 않으면 {"available":true}
    중복이면 {"available":false}
    * */
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        boolean available = userService.isEmailAvailable(email);
        // { "available": true } 또는 { "available": false }
        return ResponseEntity.ok(Map.of("available", available));
    }
}
