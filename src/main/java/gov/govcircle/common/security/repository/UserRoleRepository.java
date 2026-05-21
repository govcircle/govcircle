package gov.govcircle.common.security.repository;

import gov.govcircle.common.security.model.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    List<UserRole> findByUserId(Long userId);

    @Query(value = "SELECT userRole FROM UserRole userRole WHERE userRole.revoked = false AND userRole.keyIdentifier = :keyIdentifier")
    Optional<UserRole> findByActiveKeyIdentifier(String keyIdentifier);

    List<UserRole> findByKeyIdentifier(String keyIdentifier);

    Optional<UserRole> findByUserIdAndKeyIdentifier(
            Long userId,
            String keyIdentifier
    );

}
