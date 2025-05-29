package gov.govcircle;

import gov.govcircle.comon.security.model.entity.Role;

import gov.govcircle.comon.security.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class GovCircleApplication implements CommandLineRunner {


	private final RoleRepository roleRepository;

    public GovCircleApplication(
			RoleRepository roleRepository

	) {
		this.roleRepository = roleRepository;

    }

    public static void main(String[] args) {
		SpringApplication.run(GovCircleApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		System.out.println("started");

		Role simpleRole = new Role();
		simpleRole.setTitle("SIMPLE");
		roleRepository.save(simpleRole);

		System.out.println("started");

	}
}
