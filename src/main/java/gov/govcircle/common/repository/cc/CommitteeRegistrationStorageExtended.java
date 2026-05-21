package gov.govcircle.common.repository.cc;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.CommitteeRegistrationEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommitteeRegistrationStorageExtended {
    List<CommitteeRegistrationEntity> findRegistrationByCommitteeColdKey(
            String committeeColdKey,
            Pageable pageable
    );

}
