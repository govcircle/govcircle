package gov.govcircle.common.security.model.mapper.entitydto;

import gov.govcircle.common.security.model.dto.RoleAuthorityDTO;
import gov.govcircle.common.security.model.entity.RoleAuthority;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = RoleAuthorityEntityDTOMapperHelper.class
)
public interface RoleAuthorityEntityDTOMapper extends BaseEntityDTOMapper<RoleAuthority, RoleAuthorityDTO> {

    RoleAuthorityDTO toDTO(RoleAuthority entity);
    RoleAuthority toEntity(RoleAuthorityDTO dto);
    List<RoleAuthorityDTO> toDTO(List<RoleAuthority> entityList);
    List<RoleAuthority> toEntity(List<RoleAuthorityDTO> dtoList);

}
