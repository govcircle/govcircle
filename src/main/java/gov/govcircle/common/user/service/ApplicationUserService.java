package gov.govcircle.common.user.service;

import gov.govcircle.common.security.model.dto.AddActorDataSignatureDTO;
import gov.govcircle.common.security.model.dto.ApplicationUserDTO;
import gov.govcircle.common.security.model.dto.UpdateProfileDTO;
import gov.govcircle.common.security.model.entity.ApplicationUser;
import gov.govcircle.common.security.model.vo.DataSignatureVO;

import java.util.Optional;

public interface ApplicationUserService {

    Optional<ApplicationUserDTO> findByUserIdentifier(String identifier);
    ApplicationUserDTO addActorRoleToUser(AddActorDataSignatureDTO addActorDataSignatureDTO);
    Optional<ApplicationUserDTO> updateUserProfile(UpdateProfileDTO updateProfileDTO);
    ApplicationUser getReferenceById(Long id);
    ApplicationUser save(ApplicationUser user);
}
