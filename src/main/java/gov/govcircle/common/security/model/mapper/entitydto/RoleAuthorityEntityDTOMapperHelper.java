package gov.govcircle.common.security.model.mapper.entitydto;

import gov.govcircle.common.security.model.dto.RoleAuthorityDTO;
import gov.govcircle.common.security.model.entity.RoleAuthority;
import org.mapstruct.BeforeMapping;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class RoleAuthorityEntityDTOMapperHelper {

    @BeforeMapping
    void setRoleAuthorities(RoleAuthority entity) {
        entity
                .getRole()
                .setAuthorities(Collections.emptyList());

    }

    @BeforeMapping
    void setRoleDTOAuthorities(RoleAuthorityDTO dto) {
        dto
                .getRole()
                .setAuthorities(Collections.emptyList());

    }

}
