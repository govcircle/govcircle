package gov.govcircle.common.repository.drep;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.DRepRegistrationEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class DRepRegistrationStorageExtendedImpl implements DRepRegistrationStorageExtended {
//    @PersistenceContext
//    private EntityManager entityManager;

    @Override
    public List<DRepRegistrationEntity> findRegistrationByDRepId(
            String dRepId,
            Pageable pageable
    ) {
//        return entityManager
//                .createQuery(
//                        "SELECT d FROM DRepRegistrationEntity d " +
//                                "WHERE d.drepId = :dRepId " +
//                                "ORDER BY d.slot DESC ",
//                        DRepRegistrationEntity.class
//                )
//                .getResultList();
        return Collections.emptyList();

    }

}
