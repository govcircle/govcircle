package gov.govcircle;

import gov.govcircle.common.security.repository.AuthorityRepository;
import gov.govcircle.common.security.repository.RoleAuthorityRepository;
import gov.govcircle.common.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan(
		basePackages = {
				// Application
				"gov.govcircle.common.security.model.entity",

				// Constitution
				"gov.govcircle.constitution.constitution.model.entities",

				// Opinion
				"gov.govcircle.constitution.opinion.model.entities",

				// Rule
				"gov.govcircle.constitution.rule.model.entities"
		}
)
@EnableJpaRepositories(
		basePackages = {
				// Application
				"gov.govcircle.common.user.repository",
				"gov.govcircle.common.security.repository",

				// Constitution
				"gov.govcircle.constitution.constitution.repository",

				// Opinion
				"gov.govcircle.constitution.opinion.repository",

				// Rule
				"gov.govcircle.constitution.rule.repository"
		},
		entityManagerFactoryRef = "entityManagerFactory",
		transactionManagerRef = "govCircleTransactionManager"

)
@RequiredArgsConstructor
public class GovCircleApplication implements CommandLineRunner {


//	private final StakeAddressService stakeAddressService;
	private final RoleAuthorityRepository roleAuthorityRepository;
	private final AuthorityRepository authorityRepository;
	private final RoleRepository roleRepository;

    public static void main(String[] args) {
		SpringApplication.run(GovCircleApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		System.out.println("GovCircle Started");

	}
}
