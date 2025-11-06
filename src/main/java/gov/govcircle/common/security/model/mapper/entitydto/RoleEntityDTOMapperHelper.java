package gov.govcircle.common.security.model.mapper.entitydto;

import gov.govcircle.common.security.model.dto.RoleDTO;
import gov.govcircle.common.security.model.entity.Role;
import org.mapstruct.BeforeMapping;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class RoleEntityDTOMapperHelper {

    @BeforeMapping
    void setRoleAuthorities(Role entity) {
        for (
                int index = 0;
                index < entity.getAuthorities().size();
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
