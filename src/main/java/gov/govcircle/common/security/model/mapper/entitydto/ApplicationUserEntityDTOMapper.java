package gov.govcircle.common.security.model.mapper.entitydto;

import gov.govcircle.common.security.model.dto.ApplicationUserDTO;
import gov.govcircle.common.security.model.dto.UserRoleDTO;
import gov.govcircle.common.security.model.entity.ApplicationUser;
import gov.govcircle.common.security.model.entity.UserAuthority;
import gov.govcircle.common.security.model.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface ApplicationUserEntityDTOMapper {

    @Mapping(
            target = "roles",
            ignore = true
    )
    @Mapping(
            target = "authorities",
            ignore = true
    )
    ApplicationUserDTO toDTO(ApplicationUser entity);
    ApplicationUser toEntity(ApplicationUserDTO dto);
    List<ApplicationUserDTO> toDTO(List<ApplicationUser> entityList);
    List<ApplicationUser> toEntity(List<ApplicationUserDTO> dtoList);


}
