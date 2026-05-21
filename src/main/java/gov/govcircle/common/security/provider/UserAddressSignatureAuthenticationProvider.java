package gov.govcircle.common.security.provider;


import com.bloxbean.cardano.client.cip.cip30.DataSignature;
import com.bloxbean.cardano.client.util.HexUtil;
import gov.govcircle.common.security.model.dto.*;
import gov.govcircle.common.security.model.entity.CardanoActorType;
import gov.govcircle.common.security.model.entity.UserRole;
import gov.govcircle.common.security.model.exception.UserAddressVerificationException;
import gov.govcircle.common.security.service.UserAuthorityService;
import gov.govcircle.common.security.service.UserRoleService;
import gov.govcircle.common.user.service.ApplicationUserService;
import gov.govcircle.common.util.GovCircleGovernanceUtils;
import gov.govcircle.common.util.GovCircleSignatureUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class UserAddressSignatureAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService inMemoryCacheUserDetailsService;
    private final ApplicationUserService applicationUserService;
    private final UserAuthorityService userAuthorityService;
    private final UserRoleService userRoleService;

    public UserAddressSignatureAuthenticationProvider(
            @Qualifier("InMemoryCacheUserDetailsService") UserDetailsService inMemoryCacheUserDetailsService,
            ApplicationUserService applicationUserService,
            UserAuthorityService userAuthorityService,
            UserRoleService userRoleService
    ) {
        this.inMemoryCacheUserDetailsService = inMemoryCacheUserDetailsService;
        this.applicationUserService = applicationUserService;
        this.userAuthorityService = userAuthorityService;
        this.userRoleService = userRoleService;

    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserAddressSignatureAuthenticationToken userAddressSignatureAuthenticationToken = (UserAddressSignatureAuthenticationToken) authentication;
        String keyString = userAddressSignatureAuthenticationToken
                .getCredentials()
                .getKey();
        String signatureString = userAddressSignatureAuthenticationToken
                .getCredentials()
                .getSignature();
        DataSignature dataSignature = new DataSignature(
                signatureString,
                keyString
        );

        String noncePayload = GovCircleSignatureUtils.getDataSignaturePayload(dataSignature);
        CardanoActorType actorType = GovCircleSignatureUtils.getDataSignatureActorType(dataSignature);
        byte[] cip8AddressBytes = GovCircleSignatureUtils.getCip8AddressBytesFromDataSignature(dataSignature);
        byte[] publicKeyHash = GovCircleSignatureUtils.getCip8PublicKeyHashFromDataSignature(dataSignature);
        String keyIdentifier = GovCircleSignatureUtils.getKeyIdentifierFromDataSignature(dataSignature);

        boolean verified = GovCircleSignatureUtils.verifySignature(
                dataSignature,
                true,
                ki -> (InMemoryActorAuthenticationIdentifierDTO) inMemoryCacheUserDetailsService.loadUserByUsername(ki)
        );
        if (!verified) {
            throw new UserAddressVerificationException("the provided data signature is invalid");

        }
        if (actorType.equals(CardanoActorType.DREP) && cip8AddressBytes.length != 29) {
            keyIdentifier = GovCircleGovernanceUtils.dRepCip129fromByte(cip8AddressBytes);

        }
        ApplicationUserDTO applicationUserDTO;
        Optional<ApplicationUserDTO> applicationUserDTOContainer = applicationUserService.findByUserIdentifier(keyIdentifier);
        if (applicationUserDTOContainer.isEmpty()) {
            UserRoleDTO userRoleDTO = userRoleService.createNewUserRole(
                    new ApplicationUserDTO(),
                    actorType,
                    keyIdentifier,
                    HexUtil.encodeHexString(publicKeyHash),
                    noncePayload
            );
            UserRoleDTO savedUserRoleDTO = userRoleService.save(userRoleDTO);
            applicationUserDTO = savedUserRoleDTO.getUser();
            List<UserRoleDTO> userRoleDTOS = userRoleService.findByUserId(applicationUserDTO.getId());
            applicationUserDTO.setRoles(userRoleDTOS);
            List<UserAuthorityDTO> userAuthorityDTOS = userAuthorityService.findByUserId(applicationUserDTO.getId());
            applicationUserDTO.setAuthorities(userAuthorityDTOS);

        } else {
            applicationUserDTO = applicationUserDTOContainer.get();

        }
        UserDetailsInfoDTO userDetailsInfoDTO = new UserDetailsInfoDTO(
                applicationUserDTO,
                keyIdentifier
        );
        return createAuthentication(
                signatureString,
                keyString,
                userDetailsInfoDTO
        );

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UserAddressSignatureAuthenticationToken.class);

    }

    private UserAddressSignatureAuthenticationToken createAuthentication(
            String signature,
            String key,
            UserDetails userDetails
    ) {
        UserAddressSignatureAuthenticationToken authenticationToken = new UserAddressSignatureAuthenticationToken(
                null,
                userDetails
        );
        authenticationToken.setAuthenticated(true);
        return authenticationToken;

    }

}

