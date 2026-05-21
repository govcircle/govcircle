package gov.govcircle.common.repository.spo;

import com.bloxbean.cardano.yaci.store.staking.storage.impl.model.PoolRegistrationEnity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PoolRegistrationStorageExtended {
    List<PoolRegistrationEnity> findRegistrationByPoolId(
            String poolId,
            Pageable pageable
    );

}
