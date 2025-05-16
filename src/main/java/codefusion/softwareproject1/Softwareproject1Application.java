package codefusion.softwareproject1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableJpaAuditing

public class Softwareproject1Application {

	public static void main(String[] args) {
		
		SpringApplication.run(Softwareproject1Application.class, args);
	}

}
