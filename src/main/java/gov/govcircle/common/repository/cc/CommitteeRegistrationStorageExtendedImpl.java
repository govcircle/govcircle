package gov.govcircle.common.repository.cc;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.CommitteeRegistrationEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CommitteeRegistrationStorageExtendedImpl implements CommitteeRegistrationStorageExtended {
//    @PersistenceContext
//    private EntityManager entityManager;
    @Override
    public List<CommitteeRegistrationEntity> findRegistrationByCommitteeColdKey(
            String committeeColdKey,
            Pageable pageable
    ) {
//        return entityManager
//                .createQuery(
//                        "SELECT c FROM CommitteeRegistrationEntity c " +
//                                "WHERE c.coldKey = :committeeColdKey " +
//                                "ORDER BY c.slot DESC ",
//                        CommitteeRegistrationEntity.class
//                )
//                .getResultList();
        return Collections.emptyList();

    }

}
