package gov.govcircle.comon.security.endpoint.controller;

import gov.govcircle.comon.config.Configs;
import gov.govcircle.comon.security.model.dto.JWTTokenResponse;
import gov.govcircle.comon.security.model.dto.NonceResponse;
import gov.govcircle.comon.security.model.dto.UserDetailsInfoDTO;
import gov.govcircle.comon.security.model.entity.ApplicationUser;
import gov.govcircle.comon.security.model.entity.Role;
import gov.govcircle.comon.security.model.entity.UserRole;
import gov.govcircle.comon.security.model.entity.UserVerificationStatus;
import gov.govcircle.comon.security.model.exception.UserAddressNotFoundException;
import gov.govcircle.comon.security.repository.RoleRepository;
import gov.govcircle.comon.security.repository.UserRoleRepository;
import gov.govcircle.comon.security.service.JWTService;
import gov.govcircle.comon.serviec.BaseService;
import gov.govcircle.comon.user.repository.ApplicationUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController("/")
@RequiredArgsConstructor
public class AuthenticationController {

    private final ApplicationUserRepository applicationUserRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final BaseService baseService;
    private final JWTService jwtService;

    @PostMapping("verify-signature")
    public ResponseEntity<?> verify(
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request
    ) {
        ApplicationUser applicationUser = applicationUserRepository.findByUserAddress(userDetails.getUsername())
                .orElseThrow(() -> new UserAddressNotFoundException("No user found with provided address"));
        applicationUser.setStatus(UserVerificationStatus.VERIFIED);
        applicationUser = (ApplicationUser) baseService.fillUpdate(applicationUser);
        applicationUserRepository.save(applicationUser);
        return ResponseEntity.ok(
                new JWTTokenResponse(
                        jwtService.generateToken((UserDetailsInfoDTO) userDetails)
                )
        );
    }

    @Transactional
    @PostMapping("generate-nonce")
    public ResponseEntity<?> login(
            @RequestBody String uniqueAddress,
            HttpServletRequest request
    ) {
        String nonce = UUID.randomUUID().toString();

        if (Objects.isNull(uniqueAddress)) {
            throw new RuntimeException("Address is null");

        }
        Optional<ApplicationUser> applicationUserContainer = applicationUserRepository.findByUserAddress(uniqueAddress);
        Optional<Role> roleContainer = roleRepository.findByTitle(Configs.ROLE);
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
        applicationUser = applicationUserRepository.save(applicationUser);

        if (applicationUserContainer.isEmpty()) {
            Role role = roleContainer.orElseGet(Role::defaultRole);
            UserRole userRole = new UserRole();
            userRole.setUser(applicationUser);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }

        return ResponseEntity.ok(new NonceResponse(nonce));

    }


}
