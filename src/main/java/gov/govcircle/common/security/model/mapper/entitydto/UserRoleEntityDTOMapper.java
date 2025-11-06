package gov.govcircle.common.security.model.mapper.entitydto;

import gov.govcircle.common.security.model.dto.UserRoleDTO;
import gov.govcircle.common.security.model.entity.UserRole;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface UserRoleEntityDTOMapper {

    UserRoleDTO toDTO(UserRole entity);
    UserRole toEntity(UserRoleDTO dto);
    List<UserRoleDTO> toDTO(List<UserRole> entityList);
    List<UserRole> toEntity(List<UserRoleDTO> dtoList);

}
