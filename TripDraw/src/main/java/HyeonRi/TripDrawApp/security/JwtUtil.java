package HyeonRi.TripDrawApp.security;

import HyeonRi.TripDrawApp.domain.LoginType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET;

    private final long ACCESS_EXP_MS  = 1000 * 60 * 60 * 2;      // 2시간
    private final long REFRESH_EXP_MS = 1000 * 60 * 60 * 24 * 7; // 7일

    public String generateAccessToken(String email, LoginType loginType) {
        return Jwts.builder()
                .setSubject(email)
                .claim("loginType", loginType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXP_MS))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP_MS))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public LoginType getLoginTypeFromToken(String token) {
        Claims body = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        String type = body.get("loginType", String.class);
        return LoginType.valueOf(type);
    }

//    public Long getUserIdFromContext() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails)) {
//            throw new IllegalStateException("인증 정보가 없습니다.");
//        }
//        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
//        return user.getUser().getUserId();
//    }

}


