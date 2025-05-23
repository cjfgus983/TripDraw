package HyeonRi.TripDrawApp.controller;

import HyeonRi.TripDrawApp.domain.LoginType;
import HyeonRi.TripDrawApp.dto.LoginRequestDto;
import HyeonRi.TripDrawApp.dto.LoginResponseDto;
import HyeonRi.TripDrawApp.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

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
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request, HttpServletResponse response) {
        // 1) 인증
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2) 토큰 생성
        String accessToken  = jwtUtil.generateAccessToken(request.getEmail(), LoginType.GENERAL);
        String refreshToken = jwtUtil.generateRefreshToken(request.getEmail());

        // 3) 리프레시 토큰을 HttpOnly 쿠키로 세팅
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path("/")                // 리프레시 엔드포인트 경로에만 자동 전송
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 4) 액세스 토큰을 바디에 반환
        return ResponseEntity.ok(new LoginResponseDto(accessToken));
    }

    /** 리프레시 토큰으로 새 액세스 토큰 발급 */
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@CookieValue(name = "refreshToken", required = false) String rt) {
        if (rt == null || !jwtUtil.validateToken(rt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = jwtUtil.getEmailFromToken(rt);
        String newAccessToken = jwtUtil.generateAccessToken(email, LoginType.GENERAL);
        return ResponseEntity.ok(new LoginResponseDto(newAccessToken));
    }

    /** 로그아웃 시 쿠키 삭제 */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse resp) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        resp.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }
}
