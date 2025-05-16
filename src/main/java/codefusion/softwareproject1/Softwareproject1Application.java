



package codefusion.softwareproject1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import codefusion.softwareproject1.entity.Teacher;
import codefusion.softwareproject1.repo.TeacherRepo;

@SpringBootApplication
@EnableJpaAuditing
public class Softwareproject1Application {
	@Autowired
	private static TeacherRepo	 teacherRepo;
	public static void main(String[] args) {
		
		SpringApplication.run(Softwareproject1Application.class, args);
	}

}
