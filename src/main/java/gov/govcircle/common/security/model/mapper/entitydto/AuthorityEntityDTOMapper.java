package gov.govcircle.common.security.model.mapper.entitydto;

import gov.govcircle.common.security.model.dto.AuthorityDTO;
import gov.govcircle.common.security.model.entity.Authority;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface AuthorityEntityDTOMapper extends BaseEntityDTOMapper<Authority, AuthorityDTO> {

    AuthorityDTO toDTO(Authority entity);
    Authority toEntity(AuthorityDTO dto);
    List<AuthorityDTO> toDTO(List<Authority> entityList);
    List<Authority> toEntity(List<AuthorityDTO> dtoList);
}
