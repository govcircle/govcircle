package gov.govcircle.common.security.service;

import gov.govcircle.common.security.model.dto.UserAuthorityDTO;

import java.util.List;

public interface UserAuthorityService {

    List<UserAuthorityDTO> findByUserId(Long userId);
}
