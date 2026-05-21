package gov.govcircle.common.repository.drep;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.DelegationVoteEntity;

import java.util.Optional;

public interface DelegationVoteStorageExtended {

    Optional<DelegationVoteEntity> findByAddress(String stakeAddress);
}
