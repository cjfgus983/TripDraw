package HyeonRi.TripDrawApp.security;

import HyeonRi.TripDrawApp.domain.LoginType;
import HyeonRi.TripDrawApp.domain.User;
import HyeonRi.TripDrawApp.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;            // UserDetailsService 대신

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtUtil.validateToken(token)) {
                // 1) 토큰에서 이메일과 loginType 클레임 꺼내기
                String email = jwtUtil.getEmailFromToken(token);

                LoginType loginType = jwtUtil.getLoginTypeFromToken(token);

                // 2) loginType 기준으로 DB에서 정확한 User 조회
                User user = userService.findByEmailAndLoginType(email, loginType);

                // 3) CustomUserDetails 로 wrapping
                CustomUserDetails principal = new CustomUserDetails(user);
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        principal, null, principal.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
