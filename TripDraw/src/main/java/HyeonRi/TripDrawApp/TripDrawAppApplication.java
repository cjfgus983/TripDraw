package HyeonRi.TripDrawApp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("HyeonRi.TripDrawApp.mapper")
public class TripDrawAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripDrawAppApplication.class, args);
	}
//
}
