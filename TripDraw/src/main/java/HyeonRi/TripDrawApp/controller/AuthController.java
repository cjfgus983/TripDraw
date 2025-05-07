package HyeonRi.TripDrawApp.controller;

import HyeonRi.TripDrawApp.dto.LoginRequest;
import HyeonRi.TripDrawApp.dto.LoginResponse;
import HyeonRi.TripDrawApp.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/*
* 로그인, jwt 발급 위함
*
* */

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        String token = jwtUtil.generateToken(request.getEmail());

        // 두 번째 인자로 토큰 타입을 넣어 주는 예시
        return ResponseEntity.ok(
                new LoginResponse(
                        token,
                        "Bearer"      // LoginResponse 생성자가 (String token, String type) 순서라면
                )
        );
    }
}
