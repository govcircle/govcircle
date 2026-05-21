package gov.govcircle.common.security.service;

import gov.govcircle.common.security.model.dto.UserAuthorityDTO;
import gov.govcircle.common.security.model.entity.UserAuthority;
import gov.govcircle.common.security.model.mapper.entitydto.UserAuthorityEntityDTOMapper;
import gov.govcircle.common.security.repository.UserAuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthorityServiceImpl implements UserAuthorityService {
    private final UserAuthorityRepository userAuthorityRepository;
    private final UserAuthorityEntityDTOMapper userAuthorityEntityDTOMapper;

    @Override
    public List<UserAuthorityDTO> findByUserId(Long userId) {
        List<UserAuthority> userAuthorities = userAuthorityRepository.findByUserId(userId);
        return userAuthorityEntityDTOMapper.toDTO(userAuthorities);

    }

}
