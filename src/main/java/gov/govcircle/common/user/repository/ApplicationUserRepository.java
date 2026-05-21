package gov.govcircle.common.user.repository;

import gov.govcircle.common.security.model.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

    @Query(value = "SELECT userRole.user FROM UserRole userRole WHERE userRole.revoked = false AND userRole.keyIdentifier = :identifier")
    Optional<ApplicationUser> findByUserIdentifier(@PathVariable String identifier);

}
