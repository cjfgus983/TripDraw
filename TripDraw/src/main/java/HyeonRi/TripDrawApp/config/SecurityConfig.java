package HyeonRi.TripDrawApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 제거

                .cors(Customizer.withDefaults()) // cors 설정 허가

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login/**", "/oauth2/**", "/api/**").permitAll()
                        // 위 url들에 대해서는 인증 없이 접근 가능 -> 로그인 필요 없음
                        .anyRequest().authenticated()
                        // 이외에는 싹 다 인증 없으면 막기
                );

//                .oauth2Login(oauth2 -> oauth2
//                        .defaultSuccessUrl("/api/home", true) //
//                );

        return http.build();
    }
}
