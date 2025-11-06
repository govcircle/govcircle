package gov.govcircle.common.repository.drep;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.DelegationVoteEntity;
import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.DelegationVoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomDelegationVoteRepository extends JpaRepository<DelegationVoteEntity, DelegationVoteId> {

    Optional<DelegationVoteEntity> findByAddress(String stakeAddress);

}
