package gov.govcircle.common.security.service;

import com.bloxbean.cardano.yaci.core.model.certs.CertificateType;
import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.CommitteeRegistrationEntity;
import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.DRepRegistrationEntity;
import com.bloxbean.cardano.yaci.store.staking.storage.impl.model.PoolRegistrationEnity;
import com.bloxbean.cardano.yaci.store.utxo.storage.impl.model.AddressUtxoEntity;
import gov.govcircle.common.config.Configs;
import gov.govcircle.common.models.exception.ContentNotFoundException;
import gov.govcircle.common.repository.cc.CommitteeRegistrationStorageExtended;
import gov.govcircle.common.repository.drep.DRepRegistrationStorageExtended;
import gov.govcircle.common.repository.spo.PoolRegistrationStorageExtended;
import gov.govcircle.common.repository.utxo.AddressUtxoStorageExtended;
import gov.govcircle.common.security.model.dto.ApplicationUserDTO;
import gov.govcircle.common.security.model.dto.RoleDTO;
import gov.govcircle.common.security.model.dto.UserRoleDTO;
import gov.govcircle.common.security.model.entity.*;
import gov.govcircle.common.security.model.mapper.entitydto.RoleEntityDTOMapper;
import gov.govcircle.common.security.model.mapper.entitydto.UserRoleEntityDTOMapper;
import gov.govcircle.common.security.repository.RoleRepository;
import gov.govcircle.common.security.repository.UserRoleRepository;
import gov.govcircle.common.user.repository.ApplicationUserRepository;
import gov.govcircle.common.util.GovCircleUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final CommitteeRegistrationStorageExtended committeeRegistrationStorageExtended;
    private final PoolRegistrationStorageExtended poolRegistrationStorageExtended;
    private final DRepRegistrationStorageExtended dRepRegistrationStorageExtended;
    private final AddressUtxoStorageExtended addressUtxoStorageExtended;
    private final ApplicationUserRepository applicationUserRepository;
    private final UserRoleEntityDTOMapper userRoleEntityDTOMapper;
    private final RoleEntityDTOMapper roleEntityDTOMapper;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<UserRoleDTO> findByKeyIdentifier(String keyIdentifier) {
        List<UserRole> userRoleList = userRoleRepository.findByKeyIdentifier(keyIdentifier);
        return userRoleEntityDTOMapper.toDTO(userRoleList);

    }
    @Override
    public Optional<UserRoleDTO> findByUserIdAndKeyIdentifier(
            Long userId,
            String keyIdentifier
    ) {
        return userRoleRepository
                .findByUserIdAndKeyIdentifier(
                        userId,
                        keyIdentifier
                )
                .map(userRoleEntityDTOMapper::toDTO);

    }
    @Override
    public void revokeUserRolesWithGivenIdentifier(String keyIdentifier) {
        Optional<UserRole> userRoleContainer = userRoleRepository.findByActiveKeyIdentifier(keyIdentifier);
        if (userRoleContainer.isPresent()) {
            UserRole userRole = userRoleContainer.get();
            userRole.setRevoked(Boolean.TRUE);
            userRoleRepository.save(userRole);

        }

    }
    @Override
    public List<UserRoleDTO> getOnChainUserRoles(
            String userAddress
    ) {
        List<UserRoleDTO> userRoles = new ArrayList<>();
        checkIfUserIsDRep(userAddress)
                .ifPresent(userRoles::add);

        //TODO: check if user is SPO
        //TODO: check if user is CC

        return userRoles;

    }

    @Override
    public Optional<UserRoleDTO> checkIfUserIsDRep(
            String userAddress
    ) {
        PageRequest page = PageRequest.of(
                0,
                1
        );
        List<DRepRegistrationEntity> dRepRegistrationContainer = dRepRegistrationStorageExtended.findRegistrationByDRepId(
                userAddress,
                page
        );
        if (Objects.isNull(dRepRegistrationContainer) || dRepRegistrationContainer.isEmpty()) {
            return Optional.empty();

        }
        DRepRegistrationEntity dRepRegistrationEntity = dRepRegistrationContainer.getFirst();

        boolean isUserDRep = !Objects.equals(
                dRepRegistrationEntity
                        .getType()
                        .name(),
                CertificateType
                        .UNREG_DREP_CERT
                        .name()
        );
        if (isUserDRep) {
            Optional<Role> roleContainer = roleRepository.findById(Configs.ROLES.DREP_ROLE_ID);
            if (roleContainer.isEmpty()) {
                throw new ContentNotFoundException("DRep ID provided not found");

            }
            Role role = roleContainer.get();
            RoleDTO roleDTO = roleEntityDTOMapper.toDTO(role);
            return Optional.of(
                    UserRoleDTO.builder()
                    .role(roleDTO)
                    .startSlot(dRepRegistrationEntity.getSlot())
                    .actorRegistrationStatus(
                            RoleRegistrationStatus.fromRegistrationStatus(
                                    dRepRegistrationEntity.getType()
                            )
                    )
                    .build()
            );


        } else {
            return Optional.empty();

        }

    }

    @Override
    @Transactional
    public UserRoleDTO save(UserRoleDTO userRoleDTO) {
        UserRole userRole = userRoleEntityDTOMapper.toEntity(userRoleDTO);
        Role role = userRole.getRole();
        if (Objects.nonNull(role)) {
            role = GovCircleUtils.doseEntityExistsInDB(role)
                    ? roleRepository.getReferenceById(role.getId())
                    : roleRepository.save(role);
            userRole.setRole(role);

        }
        ApplicationUser applicationUser = userRole.getUser();
        if (Objects.nonNull(applicationUser)){
            applicationUser = GovCircleUtils.doseEntityExistsInDB(applicationUser)
                    ? applicationUserRepository.getReferenceById(applicationUser.getId())
                    : applicationUserRepository.save(applicationUser);
            userRole.setUser(applicationUser);

        }
        UserRole saved = userRoleRepository.save(userRole);
        return userRoleEntityDTOMapper.toDTO(saved);

    }

    @Override
    public List<UserRoleDTO> findByUserId(Long userId) {
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);
        return userRoleEntityDTOMapper.toDTO(userRoles);

    }

    @Override
    public UserRoleDTO createNewUserRole(
            ApplicationUserDTO applicationUserDTO,
            CardanoActorType cardanoActorType,
            String keyIdentifier,
            String publicKeyHash,
            String nonce
    ) {
        applicationUserDTO.setNonce(nonce);
        RoleDTO roleDTO = RoleDTO.getCardanoRole(cardanoActorType);
        PageRequest page = PageRequest.of(
                0,
                1
        );
        return switch (cardanoActorType) {
            case CC -> {
                List<CommitteeRegistrationEntity> committeeRegistrations = committeeRegistrationStorageExtended.findRegistrationByCommitteeColdKey(
                        keyIdentifier,
                        page
                );
                yield committeeRegistrations.isEmpty()
                        ? new UserRoleDTO(
                        roleDTO,
                        applicationUserDTO,
                        publicKeyHash,
                        keyIdentifier
                )
                        : new UserRoleDTO(
                        null,
                        roleDTO,
                        applicationUserDTO,
                        null,
                        publicKeyHash,
                        keyIdentifier,
                        Boolean.FALSE,
                        committeeRegistrations
                                .getFirst()
                                .getSlot(),
                        null
                );

            }
            case SPO -> {
                List<PoolRegistrationEnity> poolRegistrations = poolRegistrationStorageExtended.findRegistrationByPoolId(
                        keyIdentifier,
                        page
                );
                yield poolRegistrations.isEmpty()
                        ? new UserRoleDTO(
                        roleDTO,
                        applicationUserDTO,
                        publicKeyHash,
                        keyIdentifier
                )
                        : new UserRoleDTO(
                        null,
                        roleDTO,
                        applicationUserDTO,
                        null,
                        publicKeyHash,
                        keyIdentifier,
                        Boolean.FALSE,
                        poolRegistrations
                                .getFirst()
                                .getSlot(),
                        null
                );

            }
            case DREP -> {
                List<DRepRegistrationEntity> dRepRegistrations = dRepRegistrationStorageExtended.findRegistrationByDRepId(
                        keyIdentifier,
                        page
                );
                yield dRepRegistrations.isEmpty()
                        ? new UserRoleDTO(
                        roleDTO,
                        applicationUserDTO,
                        publicKeyHash,
                        keyIdentifier
                )
                        : new UserRoleDTO(
                        null,
                        roleDTO,
                        applicationUserDTO,
                        RoleRegistrationStatus.fromRegistrationStatus(
                                dRepRegistrations
                                        .getFirst()
                                        .getType()
                        ),
                        publicKeyHash,
                        keyIdentifier,
                        Boolean.FALSE,
                        dRepRegistrations
                                .getFirst()
                                .getSlot(),
                        null
                );

            }
            case WALLET -> {
                List<AddressUtxoEntity> addressUtxos = addressUtxoStorageExtended.findAddressUtxoByStakeAddress(
                        keyIdentifier,
                        page
                );
                yield addressUtxos.isEmpty()
                        ? new UserRoleDTO(
                        roleDTO,
                        applicationUserDTO,
                        publicKeyHash,
                        keyIdentifier
                )
                        : new UserRoleDTO(
                        null,
                        roleDTO,
                        applicationUserDTO,
                        null,
                        publicKeyHash,
                        keyIdentifier,
                        Boolean.FALSE,
                        addressUtxos
                                .getFirst()
                                .getSlot(),
                        null
                );

            }

        };

    }

}
