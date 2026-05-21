package gov.govcircle.common.repository.drep;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.DelegationVoteEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DelegationVoteStorageExtendedImpl implements DelegationVoteStorageExtended {
//    @PersistenceContext
//    private EntityManager entityManager;

    @Override
    public Optional<DelegationVoteEntity> findByAddress(String stakeAddress) {
//        DelegationVoteEntity delegationVoteEntity = entityManager.createQuery(
//                "SELECT d FROM DelegationVoteEntity d " +
//                        "WHERE d.address = :stakeAddress ",
//                DelegationVoteEntity.class
//        ).getSingleResult();
//        return delegationVoteEntity != null
//                ? Optional.of(delegationVoteEntity)
//                : Optional.empty();
        return Optional.empty();

    }

}
