package gov.govcircle.common.security.model.mapper.entitydto;

import gov.govcircle.common.security.model.dto.UserAuthorityDTO;
import gov.govcircle.common.security.model.entity.UserAuthority;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface UserAuthorityEntityDTOMapper extends BaseEntityDTOMapper<UserAuthority, UserAuthorityDTO> {

    UserAuthorityDTO toDTO(UserAuthority entity);
    UserAuthority toEntity(UserAuthorityDTO dto);
    List<UserAuthorityDTO> toDTO(List<UserAuthority> entityList);
    List<UserAuthority> toEntity(List<UserAuthorityDTO> dtoList);
}
