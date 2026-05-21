package gov.govcircle.common.security.service;

import gov.govcircle.common.security.model.dto.RoleAuthorityDTO;
import gov.govcircle.common.security.model.entity.RoleAuthority;
import gov.govcircle.common.security.model.mapper.entitydto.RoleAuthorityEntityDTOMapper;
import gov.govcircle.common.security.repository.RoleAuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleAuthorityServiceImpl implements RoleAuthorityService {
    private final RoleAuthorityRepository roleAuthorityRepository;
    private final RoleAuthorityEntityDTOMapper roleAuthorityEntityDTOMapper;

    @Override
    public List<RoleAuthorityDTO> findByRoleId(Long roleId) {
        List<RoleAuthority> roleAuthorities = roleAuthorityRepository.findByRoleId(roleId);
        return roleAuthorityEntityDTOMapper.toDTO(roleAuthorities);

    }

}
