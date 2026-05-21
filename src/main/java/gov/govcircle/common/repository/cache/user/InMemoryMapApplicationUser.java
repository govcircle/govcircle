package gov.govcircle.common.repository.cache.user;

import gov.govcircle.common.config.Configs;
import gov.govcircle.common.models.exception.ContentNotFoundException;
import gov.govcircle.common.security.model.dto.InMemoryActorAuthenticationIdentifierDTO;
import gov.govcircle.common.util.GovCircleUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryMapApplicationUser implements ApplicationUserCache {


    @Override
    public Optional<InMemoryActorAuthenticationIdentifierDTO> findByIdentifier(String identifier) {
        InMemoryActorAuthenticationIdentifierDTO applicationUser = Configs.identifierRequestMap
                .get(identifier);
        return Objects.nonNull(applicationUser)
                ? Optional.of(applicationUser)
                : Optional.empty();

    }

    @Override
    public void putInMap(
            String nonce,
            InMemoryActorAuthenticationIdentifierDTO inMemoryActorAuthenticationIdentifierDTO
    ) {
        if (GovCircleUtils.isNullOrEmpty(nonce)) {
            throw new ContentNotFoundException("Nonce is null or empty");

        }
        if (Objects.isNull(inMemoryActorAuthenticationIdentifierDTO)) {
            throw new ContentNotFoundException("User dose not exist");

        }
        if (GovCircleUtils.isNullOrEmpty(inMemoryActorAuthenticationIdentifierDTO.getUsername())) {
            throw new ContentNotFoundException("User dose not have key Identifier");

        }
        Configs.identifierRequestMap.put(
                inMemoryActorAuthenticationIdentifierDTO.getUsername(),
                inMemoryActorAuthenticationIdentifierDTO
        );

    }

    @Override
    public void removeFromMapByIdentifier(String identifier) {
        Optional<InMemoryActorAuthenticationIdentifierDTO> details = findByIdentifier(identifier);
        if (details.isPresent()) {
            Configs.identifierRequestMap.remove(identifier);
            return;

        }
        log.info(
                "The no such record found for identifier: {}",
                identifier
        );


    }


}
