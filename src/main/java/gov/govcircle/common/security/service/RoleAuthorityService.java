package gov.govcircle.common.security.service;


import gov.govcircle.common.security.model.dto.RoleAuthorityDTO;

import java.util.List;

public interface RoleAuthorityService {

    List<RoleAuthorityDTO> findByRoleId(Long roleId);
}
