package gov.govcircle;

import gov.govcircle.common.indexer.dbsync.model.entity.StakeAddress;
import gov.govcircle.common.indexer.dbsync.service.StakeAddressService;
import gov.govcircle.common.security.model.entity.Role;

import gov.govcircle.common.security.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Optional;


@SpringBootApplication
public class GovCircleApplication implements CommandLineRunner {


	private final StakeAddressService stakeAddressService;
	private final RoleRepository roleRepository;

    public GovCircleApplication(
			RoleRepository roleRepository,
			StakeAddressService stakeAddressService
	) {
		this.roleRepository = roleRepository;
		this.stakeAddressService = stakeAddressService;

    }

    public static void main(String[] args) {
		SpringApplication.run(GovCircleApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

	System.out.println("GovCircle Started");

	}
}
