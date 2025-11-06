package gov.govcircle.common.security.model.mapper.entitydto;

import gov.govcircle.common.security.model.dto.ApplicationUserDTO;
import gov.govcircle.common.security.model.entity.ApplicationUser;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface ApplicationUserDTOEntityDTOMapper extends BaseEntityDTOMapper<ApplicationUser, ApplicationUserDTO> {


}
