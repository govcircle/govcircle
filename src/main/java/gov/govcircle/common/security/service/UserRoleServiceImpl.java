package gov.govcircle.common.security.service;

import com.bloxbean.cardano.yaci.core.model.certs.CertificateType;
import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.DRepRegistrationEntity;
import gov.govcircle.common.config.Configs;
import gov.govcircle.common.models.exception.ContentNotFoundException;
import gov.govcircle.common.repository.drep.CustomDRepRegistrationRepository;
import gov.govcircle.common.repository.drep.CustomDelegationVoteRepository;
import gov.govcircle.common.security.model.dto.RoleDTO;
import gov.govcircle.common.security.model.dto.UserRoleDTO;
import gov.govcircle.common.security.model.entity.Role;
import gov.govcircle.common.security.model.entity.RoleRegistrationStatus;
import gov.govcircle.common.security.model.mapper.entitydto.RoleEntityDTOMapper;
import gov.govcircle.common.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final CustomDelegationVoteRepository customDelegationVoteRepository;
    private final CustomDRepRegistrationRepository customDRepRegistrationRepository;
    private final RoleService roleService;
    private final RoleEntityDTOMapper roleEntityDTOMapper;

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
        List<DRepRegistrationEntity> dRepRegistrationContainer = customDRepRegistrationRepository.findDRepByRegistrationTxOwnerAddress(
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
            Optional<Role> roleContainer = roleService.findById(Configs.DREP_ROLE_ID);
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

}
