package gov.govcircle.common.service;

import gov.govcircle.common.security.model.dto.UserDetailsInfoDTO;
import gov.govcircle.common.security.model.entity.ApplicationUser;
import gov.govcircle.common.security.model.exception.UserAddressNotFoundException;
import gov.govcircle.common.user.repository.ApplicationUserRepository;
import gov.govcircle.core.entities.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BaseServiceImpl implements BaseService {

    private final ApplicationUserRepository applicationUserRepository;

    @Override
    public BaseEntity fillSave(BaseEntity baseEntity) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (Objects.nonNull(authentication) && authentication.getPrincipal() instanceof UserDetailsInfoDTO && authentication.isAuthenticated()) {
            UserDetailsInfoDTO userDetailsInfoDTO = (UserDetailsInfoDTO) authentication.getPrincipal();
            String userAddress = userDetailsInfoDTO.getUserAddress();
            ApplicationUser applicationUser = applicationUserRepository.findByUserAddress(userAddress)
                    .orElseThrow(() -> new UserAddressNotFoundException("No user found with provided address"));
            baseEntity.setCreatedBy(applicationUser);
            baseEntity.setUpdatedBy(applicationUser);


        }
        baseEntity.setCreatedAt(LocalDateTime.now());
        baseEntity.setUpdatedAt(LocalDateTime.now());

        return baseEntity;

    }

    @Override
    public BaseEntity fillUpdate(BaseEntity baseEntity) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (Objects.nonNull(authentication) && authentication.getPrincipal() instanceof UserDetailsInfoDTO && authentication.isAuthenticated()) {
            UserDetailsInfoDTO userDetailsInfoDTO = (UserDetailsInfoDTO) authentication.getPrincipal();
            String userAddress = userDetailsInfoDTO.getUserAddress();
            ApplicationUser applicationUser = applicationUserRepository.findByUserAddress(userAddress)
                    .orElseThrow(() -> new UserAddressNotFoundException("No user found with provided address"));
            baseEntity.setCreatedBy(applicationUser);
            baseEntity.setUpdatedBy(applicationUser);


        }
        baseEntity.setUpdatedAt(LocalDateTime.now());
        return baseEntity;

    }
}
