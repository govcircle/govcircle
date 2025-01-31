package gov.govcirvle.govcircle;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GovCircleApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GovCircleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("GovCircleApplication Is Here");

	}
}
