package gov.govcircle.common.repository.drep;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.DRepRegistrationEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DRepRegistrationStorageExtended {
    List<DRepRegistrationEntity> findRegistrationByDRepId(
            String dRepId,
            Pageable pageable
    );

}
