package HyeonRi.TripDrawApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Vue가 호출하는 경로
                .allowedOrigins(
                        "https://quiet-chaja-5cea12.netlify.app",
                        "http://localhost:5173"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
    
    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	// /uploads/** 요청 → 실제 file.upload-dir 디렉터리의 파일을 반환
        registry
          .addResourceHandler("/uploads/**")
          .addResourceLocations("file:" + uploadDir + "/");
    }
}
