package gov.govcircle.common.security.service;

import gov.govcircle.common.security.model.entity.Role;

import java.util.Optional;

public interface RoleService {

    Optional<Role> findById(Long id);
    Role save(Role role);
    Role getReferenceById(Long id);

}
