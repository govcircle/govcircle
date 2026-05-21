package gov.govcircle.common.security.repository;

import gov.govcircle.common.security.model.entity.RoleAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleAuthorityRepository extends JpaRepository<RoleAuthority, Long> {

    List<RoleAuthority> findByRoleId(Long roleId);
}
