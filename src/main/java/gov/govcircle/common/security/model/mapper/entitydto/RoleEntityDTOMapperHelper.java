package gov.govcircle.common.security.model.mapper.entitydto;

import gov.govcircle.common.security.model.dto.RoleDTO;
import gov.govcircle.common.security.model.entity.Role;
import gov.govcircle.common.security.model.entity.RoleAuthority;
import gov.govcircle.common.security.repository.RoleAuthorityRepository;
import gov.govcircle.common.security.service.RoleAuthorityService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.BeforeMapping;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleEntityDTOMapperHelper {
    private final RoleAuthorityRepository roleAuthorityRepository;

    @BeforeMapping
    void setRoleAuthorities(Role entity) {
        List<RoleAuthority> roleAuthorityList = roleAuthorityRepository.findByRoleId(entity.getId());
        entity.setAuthorities(roleAuthorityList);
        for (
                int index = 0;
                index < entity
                        .getAuthorities()
                        .size();
                index++
        ) {
            entity
                    .getAuthorities()
                    .get(index)
                    .getRole()
                    .setAuthorities(Collections.emptyList());

        }

    }

    @BeforeMapping
    void setRoleDTOAuthorities(RoleDTO dto) {
        for (
                int index = 0;
                index < dto.getAuthorities().size();
                index++
        ) {
            dto
                    .getAuthorities()
                    .get(index)
                    .getRole()
                    .setAuthorities(Collections.emptyList());

        }

    }

}
