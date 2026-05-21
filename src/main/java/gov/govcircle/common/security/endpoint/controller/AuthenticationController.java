package gov.govcircle.common.security.endpoint.controller;

import gov.govcircle.common.config.Configs;
import gov.govcircle.common.repository.cache.user.ApplicationUserCache;
import gov.govcircle.common.security.model.dto.*;
import gov.govcircle.common.security.model.mapper.entitydto.UserRoleEntityDTOMapper;
import gov.govcircle.common.security.model.vo.NonceRequest;
import gov.govcircle.common.security.repository.RoleRepository;
import gov.govcircle.common.security.service.JWTService;
import gov.govcircle.common.security.service.UserRoleService;
import gov.govcircle.common.service.base.BaseService;
import gov.govcircle.common.user.service.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final ApplicationUserCache userDetailsCache;
    private final JWTService jwtService;

    @PostMapping(Configs.URLS.REST_VERIFY_SIGNATURE_ENDPOINT)
    public ResponseEntity<?> verify(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(
                new JWTTokenResponse(
                        jwtService.generateToken((UserDetailsInfoDTO) userDetails)
                )
        );
    }

    @PostMapping(Configs.URLS.REST_GENERATE_NONCE_ENDPOINT)
    public ResponseEntity<?> generateNonce(
            @RequestBody NonceRequest nonceRequest
    ) {
        String nonceCode = UUID
                .randomUUID()
                .toString();

        if (Objects.isNull(nonceRequest) || Objects.isNull(nonceRequest.getIdentifier())) {
            throw new RuntimeException("Identifier is null");

        }
        String identifier = nonceRequest.getIdentifier();
        String consent = "I agree with terms";
        String nonce = nonceRequest
                .getActorType()
                .name()
                + ":"
                + consent
                + ":"
                + nonceCode;
        InMemoryActorAuthenticationIdentifierDTO inMemoryActorAuthenticationIdentifierDTO = new InMemoryActorAuthenticationIdentifierDTO(
                identifier,
                nonce
        );
        userDetailsCache.putInMap(
                identifier,
                inMemoryActorAuthenticationIdentifierDTO
        );
        return ResponseEntity.ok(
                new NonceResponse(
                        nonceRequest.getActorType(),
                        nonce
                )
        );

    }

    @PostMapping(Configs.URLS.REST_FREE_ENDPOINT)
    public ResponseEntity<?> free(
            @RequestBody NonceRequest nonceRequest
    ) {
        return ResponseEntity.ok(
                new NonceResponse(
                        nonceRequest.getActorType(),
                        "nonceCode123!"
                )
        );

    }



}
