package gov.govcircle.common.security.model.mapper.entitydto;

import gov.govcircle.common.security.model.dto.UserRoleDTO;
import gov.govcircle.common.security.model.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                ApplicationUserEntityDTOMapper.class,
                RoleEntityDTOMapper.class
        }
)
public interface UserRoleEntityDTOMapper {

    UserRoleDTO toDTO(UserRole entity);
    UserRole toEntity(UserRoleDTO dto);
    List<UserRoleDTO> toDTO(List<UserRole> entityList);
    List<UserRole> toEntity(List<UserRoleDTO> dtoList);

}
