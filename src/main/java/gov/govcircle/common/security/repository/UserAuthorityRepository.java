package gov.govcircle.common.security.repository;

import gov.govcircle.common.security.model.entity.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
}
