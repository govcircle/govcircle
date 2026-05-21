package gov.govcircle.common.repository.spo;

import com.bloxbean.cardano.yaci.store.staking.storage.impl.model.PoolRegistrationEnity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class PoolRegistrationStorageExtendedImpl implements PoolRegistrationStorageExtended {
//    @PersistenceContext
//    private EntityManager entityManager;

    @Override
    public List<PoolRegistrationEnity> findRegistrationByPoolId(
            String poolId,
            Pageable pageable
    ) {
//        return entityManager
//                .createQuery(
//                        "SELECT p FROM PoolRegistrationEnity p " +
//                                "WHERE p.poolId = :dRepId " +
//                                "ORDER BY p.slot DESC ",
//                        PoolRegistrationEnity.class
//                )
//                .getResultList();
        return Collections.emptyList();

    }

}
