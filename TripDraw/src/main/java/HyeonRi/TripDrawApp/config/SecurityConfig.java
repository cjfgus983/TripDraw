package HyeonRi.TripDrawApp.config;

import HyeonRi.TripDrawApp.security.CustomUserDetailsService;
import HyeonRi.TripDrawApp.security.JwtAuthenticationFilter;
import HyeonRi.TripDrawApp.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    // 1) PasswordEncoder 빈
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2) AuthenticationManager 빈 등록 (UserDetailsService + PasswordEncoder 연결)
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    // 3) SecurityFilterChain 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // JWT 필터
        JwtAuthenticationFilter jwtFilter =
                new JwtAuthenticationFilter(jwtUtil, userDetailsService);

        http
                // 3-1) CSRF, CORS
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())

                // 3-2) 세션 사용 안 함 (stateless JWT)
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )


                // 3-3) URL별 접근 제어
//                .authorizeHttpRequests(auth -> auth
//                        // 회원가입, 로그인, 토큰 리프레시 등은 인증 없이 허용
//                        .requestMatchers("/api/users/signup", "/api/auth/**").permitAll()
//                        // Swagger UI, static 리소스 등도 여기에 추가 가능
//                        .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                        // 그 외엔 모두 인증 필요
//                        .anyRequest().authenticated()
//                )
                .authorizeHttpRequests(auth -> auth
                        // 회원가입·로그인·토큰 리프레시
                        .requestMatchers("/api/users/signup", "/api/auth/**").permitAll()
                        // 이메일 인증(발송/검증) 및 중복체크
                        .requestMatchers("/api/users/**").permitAll()
                        // Swagger UI, static 리소스
                        .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // 그 외는 모두 인증 필요
                        .anyRequest().authenticated()
                )


                // 3-4) JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 삽입
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
