package gov.govcircle.common.user.service;

import com.bloxbean.cardano.client.cip.cip30.DataSignature;
import com.bloxbean.cardano.client.util.HexUtil;
import gov.govcircle.common.security.model.dto.*;
import gov.govcircle.common.security.model.entity.ApplicationUser;
import gov.govcircle.common.security.model.entity.CardanoActorType;
import gov.govcircle.common.security.model.entity.UserRole;
import gov.govcircle.common.security.model.exception.InvalidSignatureException;
import gov.govcircle.common.security.model.exception.UserAddressNotFoundException;
import gov.govcircle.common.security.model.mapper.entitydto.ApplicationUserEntityDTOMapper;
import gov.govcircle.common.security.model.vo.DataSignatureVO;
import gov.govcircle.common.security.service.UserAuthorityService;
import gov.govcircle.common.security.service.UserRoleService;
import gov.govcircle.common.user.repository.ApplicationUserRepository;
import gov.govcircle.common.util.GovCircleGovernanceUtils;
import gov.govcircle.common.util.GovCircleSignatureUtils;
import gov.govcircle.common.util.GovcircleSecurityUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationUserServiceImpl implements ApplicationUserService {
    private final UserDetailsService inMemoryCacheUserDetailsService;
    private final ApplicationUserRepository applicationUserRepository;
    private final ApplicationUserEntityDTOMapper applicationUserEntityDTOMapper;
    private final UserAuthorityService userAuthorityService;
    private final UserRoleService userRoleService;

    public ApplicationUserServiceImpl(
            @Qualifier("InMemoryCacheUserDetailsService") UserDetailsService inMemoryCacheUserDetailsService,
            ApplicationUserRepository applicationUserRepository,
            ApplicationUserEntityDTOMapper applicationUserEntityDTOMapper,
            UserAuthorityService userAuthorityService,
            UserRoleService userRoleService
    ) {
        this.inMemoryCacheUserDetailsService = inMemoryCacheUserDetailsService;
        this.applicationUserRepository = applicationUserRepository;
        this.applicationUserEntityDTOMapper = applicationUserEntityDTOMapper;
        this.userAuthorityService = userAuthorityService;
        this.userRoleService = userRoleService;
    }

    @Override
    public Optional<ApplicationUserDTO> findByUserIdentifier(String identifier) {
        return applicationUserRepository
                .findByUserIdentifier(identifier)
                .map(applicationUser -> {
                            ApplicationUserDTO dto = applicationUserEntityDTOMapper.toDTO(applicationUser);
                            List<UserRoleDTO> userRoleDTOS = userRoleService.findByUserId(dto.getId());
                            dto.setRoles(userRoleDTOS);
                            List<UserAuthorityDTO> userAuthorityDTOS = userAuthorityService.findByUserId(dto.getId());
                            dto.setAuthorities(userAuthorityDTOS);
                            return dto;
                        }
                );

    }

    @Override
    @Transactional
    public ApplicationUserDTO addActorRoleToUser(
            AddActorDataSignatureDTO addActorDataSignatureDTO
    ) {
        DataSignatureVO dataSignatureVO = addActorDataSignatureDTO.getDataSignature();
        DataSignature dataSignature = new DataSignature(
                dataSignatureVO.getSignature(),
                dataSignatureVO.getKey()
        );
        boolean verified = GovCircleSignatureUtils.verifySignature(
                dataSignature,
                Boolean.TRUE,
                ki -> (InMemoryActorAuthenticationIdentifierDTO) inMemoryCacheUserDetailsService.loadUserByUsername(ki)
        );
        UserDetailsInfoDTO userDetailsInfoDTO = GovcircleSecurityUtils.getAuthenticatedActor();
        String currentUserIdentifier = userDetailsInfoDTO.getIdentifier();
        if (!verified) {
            throw new InvalidSignatureException("Provided signature is not valid");

        }
        ApplicationUser applicationUser = applicationUserRepository.findByUserIdentifier(currentUserIdentifier)
                .orElseThrow(() -> new UserAddressNotFoundException("No user found with provided address"));
        ApplicationUserDTO applicationUserDTO = applicationUserEntityDTOMapper.toDTO(applicationUser);
        String noncePayload = GovCircleSignatureUtils.getDataSignaturePayload(dataSignature);
        CardanoActorType actorType = GovCircleSignatureUtils.getDataSignatureActorType(dataSignature);
        byte[] publicKeyHash = GovCircleSignatureUtils.getCip8PublicKeyHashFromDataSignature(dataSignature);
        byte[] cip8AddressBytes = GovCircleSignatureUtils.getCip8AddressBytesFromDataSignature(dataSignature);
        String newKeyIdentifier = GovCircleSignatureUtils.getKeyIdentifierFromDataSignature(dataSignature);
        if (actorType.equals(CardanoActorType.DREP) && cip8AddressBytes.length != 29) {
            newKeyIdentifier = GovCircleGovernanceUtils.dRepCip129fromByte(cip8AddressBytes);

        }
        Optional<UserRoleDTO> userRoleDTOContainer = userRoleService.findByUserIdAndKeyIdentifier(
                applicationUser.getId(),
                newKeyIdentifier
        );
        if (userRoleDTOContainer.isPresent()) {
            UserRoleDTO userRoleDTO = userRoleDTOContainer.get();
            if (userRoleDTO.isRevoked()) {
                userRoleDTO.setRevoked(Boolean.FALSE);
                userRoleService.revokeUserRolesWithGivenIdentifier(newKeyIdentifier);
                userRoleService.save(userRoleDTO);

            }

        } else {
            userRoleService.revokeUserRolesWithGivenIdentifier(newKeyIdentifier);
            UserRoleDTO userRoleDTO = userRoleService.createNewUserRole(
                    applicationUserDTO,
                    actorType,
                    newKeyIdentifier,
                    HexUtil.encodeHexString(publicKeyHash),
                    noncePayload
            );
            userRoleService.save(userRoleDTO);

        }
        List<UserRoleDTO> userRoleDTOS = userRoleService.findByUserId(applicationUserDTO.getId());
        applicationUserDTO.setRoles(userRoleDTOS);
        List<UserAuthorityDTO> userAuthorityDTOS = userAuthorityService.findByUserId(applicationUserDTO.getId());
        applicationUserDTO.setAuthorities(userAuthorityDTOS);
        return applicationUserDTO;

    }

    @Override
    public Optional<ApplicationUserDTO> updateUserProfile(UpdateProfileDTO updateProfileDTO) {
        ApplicationUser applicationUser = applicationUserRepository.findByUserIdentifier(updateProfileDTO.getKeyIdentifier())
                .orElseThrow(() -> new UserAddressNotFoundException("No user found with provided address"));
        applicationUser.setUsername(updateProfileDTO.getUsername());
        applicationUser.setEmail(updateProfileDTO.getEmail());
        return Optional.empty();

    }

    @Override
    public ApplicationUser getReferenceById(Long id) {
        return applicationUserRepository.getReferenceById(id);

    }

    @Override
    public ApplicationUser save(ApplicationUser user) {
        return applicationUserRepository.save(user);

    }

}
