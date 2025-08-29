package gov.govcircle.common.security.repository;

import gov.govcircle.common.security.model.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    Optional<UserRole> findByUserId(Long userId);
}
