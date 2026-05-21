package gov.govcircle;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.govcircle.common.security.repository.AuthorityRepository;
import gov.govcircle.common.security.repository.RoleAuthorityRepository;
import gov.govcircle.common.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;
import java.util.Scanner;


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
				"gov.govcircle.constitution.rule.repository",

				// Core
				"gov.govcircle.common.repository"
		}
//		entityManagerFactoryRef = "entityManagerFactory",
//		transactionManagerRef = "govCircleTransactionManager"

)
@RequiredArgsConstructor
public class GovCircleApplication {

	private final RoleAuthorityRepository roleAuthorityRepository;
	private final AuthorityRepository authorityRepository;
	private final RoleRepository roleRepository;


    public static void main(String[] args) {
		System.out.println(java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments());
		System.out.println("/////////////////////////////////////#########################");
		SpringApplication.run(GovCircleApplication.class, args);

	}


	@Bean
	public DSLContext dslContext(DataSource dataSource) {
		return DSL.using(
				dataSource,
				SQLDialect.POSTGRES
		);

	}

}
