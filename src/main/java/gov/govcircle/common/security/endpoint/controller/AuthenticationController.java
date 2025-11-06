package gov.govcircle.common.security.endpoint.controller;

import gov.govcircle.common.security.model.dto.*;
import gov.govcircle.common.security.model.entity.ApplicationUser;
import gov.govcircle.common.security.model.entity.UserRole;
import gov.govcircle.common.security.model.entity.UserVerificationStatus;
import gov.govcircle.common.security.model.exception.UserAddressNotFoundException;
import gov.govcircle.common.security.model.mapper.entitydto.UserRoleEntityDTOMapper;
import gov.govcircle.common.security.repository.RoleRepository;
import gov.govcircle.common.security.service.JWTService;
import gov.govcircle.common.security.service.UserRoleService;
import gov.govcircle.common.service.base.BaseService;
import gov.govcircle.common.user.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final ApplicationUserRepository applicationUserRepository;
    private final UserRoleService userRoleService;
    private final UserRoleEntityDTOMapper userRoleEntityDTOMapper;
    private final RoleRepository roleRepository;
    private final BaseService baseService;
    private final JWTService jwtService;

    @PostMapping("/verify-signature")
    public ResponseEntity<?> verify(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ApplicationUser applicationUser = applicationUserRepository.findByUserAddress(userDetails.getUsername())
                .orElseThrow(() -> new UserAddressNotFoundException("No user found with provided address"));
        applicationUser.setStatus(UserVerificationStatus.VERIFIED);

        List<UserRole> userRoleList = applicationUser.getRoles();
        if (userRoleList.isEmpty()) {
            List<UserRoleDTO> userRoleDTOList = userRoleService.getOnChainUserRoles(applicationUser.getUserAddress());
            List<UserRole> userRoles = userRoleEntityDTOMapper.toEntity(userRoleDTOList);
            for (UserRole userRole : userRoles) {
                userRole.setUser(applicationUser);
                baseService.fillUpdate(userRole);

            }
            applicationUser.setRoles(userRoles);
            applicationUser = (ApplicationUser) baseService.fillUpdate(applicationUser);
            applicationUserRepository.save(applicationUser);

        }
        return ResponseEntity.ok(
                new JWTTokenResponse(
                        jwtService.generateToken((UserDetailsInfoDTO) userDetails)
                )
        );
    }

    @PostMapping("/generate-nonce")
    public ResponseEntity<?> login(
            @RequestBody String uniqueAddress
    ) {
        String nonce = UUID
                .randomUUID()
                .toString();

        if (Objects.isNull(uniqueAddress)) {
            throw new RuntimeException("Address is null");

        }
        Optional<ApplicationUser> applicationUserContainer = applicationUserRepository.findByUserAddress(uniqueAddress);
        ApplicationUser applicationUser = applicationUserContainer.orElseGet(() -> {
            ApplicationUser appUser = new ApplicationUser();
            appUser.setUserAddress(uniqueAddress);
            return appUser;

        });
        applicationUser.setNonce(nonce);
        applicationUser.setStatus(UserVerificationStatus.NOT_VERIFIED);
        applicationUser = applicationUserContainer.isPresent()
                ? (ApplicationUser) baseService.fillUpdate(applicationUser)
                : (ApplicationUser) baseService.fillSave(applicationUser);
        applicationUserRepository.save(applicationUser);

        return ResponseEntity.ok(new NonceResponse(nonce));

    }

}
