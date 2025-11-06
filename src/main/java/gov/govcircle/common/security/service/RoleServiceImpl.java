package gov.govcircle.common.security.service;

import gov.govcircle.common.security.model.entity.Role;
import gov.govcircle.common.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);

    }

}
