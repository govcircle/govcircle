package gov.govcircle.common.security.endpoint.controller;

import gov.govcircle.common.config.Configs;
import gov.govcircle.common.security.model.dto.AddActorDataSignatureDTO;
import gov.govcircle.common.security.model.dto.ApplicationUserDTO;
import gov.govcircle.common.security.model.dto.UpdateProfileDTO;
import gov.govcircle.common.user.service.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Configs.URLS.REST_PROFILE_PATH)
@RequiredArgsConstructor
public class UserProfileController {
    private final ApplicationUserService applicationUserService;

    @PostMapping(Configs.URLS.REST_ADD_ACTOR_SIGNATURE_ENDPOINT)
    public ResponseEntity<?> addActorSignature(
            @RequestBody AddActorDataSignatureDTO addActorDataSignatureDTO
    ) {
        ApplicationUserDTO applicationUserDTO = applicationUserService.addActorRoleToUser(addActorDataSignatureDTO);
        applicationUserDTO.setNonce(null);
        return ResponseEntity.ok(applicationUserDTO);

    }

    @PostMapping(Configs.URLS.REST_UPDATE_PROFILE_ENDPOINT)
    public ResponseEntity<?> updateProfile(
            @RequestBody AddActorDataSignatureDTO addActorDataSignatureDTO
    ) {
        ApplicationUserDTO applicationUserDTO = applicationUserService.addActorRoleToUser(addActorDataSignatureDTO);
        applicationUserDTO.setNonce(null);
        return ResponseEntity.ok(applicationUserDTO);

    }

}
