package HyeonRi.dreamTravelApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // POST 테스트 쉽게 하기 위해 CSRF 끔
                .authorizeHttpRequests()
                .anyRequest().permitAll(); // 모든 요청 허용

        return http.build();
    }
//	 @Bean
//	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//	        http
//	            .csrf().disable()
//	            .authorizeHttpRequests()
//	                .requestMatchers(
//	                    "/", 
//	                    "/index", 
//	                    "/index.jsp", 
//	                    "/css/**", 
//	                    "/js/**", 
//	                    "/images/**"
//	                ).permitAll()
//	                .anyRequest().authenticated()
//	            ;
//
//	        return http.build();
//	    }

}
