package gov.govcircle.common.repository.cache.user;

import gov.govcircle.common.security.model.dto.InMemoryActorAuthenticationIdentifierDTO;

import java.util.Optional;

public interface ApplicationUserCache {
    Optional<InMemoryActorAuthenticationIdentifierDTO> findByIdentifier(String identifier);
    void putInMap(
            String nonce,
            InMemoryActorAuthenticationIdentifierDTO inMemoryActorAuthenticationIdentifierDTO
    );
    void removeFromMapByIdentifier(String identifier);

}
