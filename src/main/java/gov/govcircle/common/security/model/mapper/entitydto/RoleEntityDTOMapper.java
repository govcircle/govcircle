package gov.govcircle.common.security.model.mapper.entitydto;

import gov.govcircle.common.security.model.dto.RoleDTO;
import gov.govcircle.common.security.model.entity.Role;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = RoleEntityDTOMapperHelper.class // this is used for custom mapping of role authorities but since we encounter lazy we decided to do not fetch the lazy collections, and instead just fill them
)
public interface RoleEntityDTOMapper extends BaseEntityDTOMapper<Role, RoleDTO> {

    @Mapping(
            target = "authorities",
            ignore = true
    )
    RoleDTO toDTO(Role entity);
    Role toEntity(RoleDTO dto);
    List<RoleDTO> toDTO(List<Role> entityList);
    List<Role> toEntity(List<RoleDTO> dtoList);

}
