package gov.govcircle.comon.user.repository;

import gov.govcircle.comon.security.model.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {


    @Query(value = "SELECT user FROM ApplicationUser user WHERE user.userAddress = :userAddress")
    Optional<ApplicationUser> findByUserAddress(@PathVariable String userAddress);

}
