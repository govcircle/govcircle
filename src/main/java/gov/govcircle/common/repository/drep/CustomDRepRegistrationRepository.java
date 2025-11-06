package gov.govcircle.common.repository.drep;

import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.DRepRegistrationEntity;
import com.bloxbean.cardano.yaci.store.governance.storage.impl.model.DRepRegistrationId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomDRepRegistrationRepository extends JpaRepository<DRepRegistrationEntity, DRepRegistrationId> {
    @Query(
            "SELECT d FROM DRepRegistrationEntity d " +
                    "JOIN AddressUtxoEntity aue ON d.txHash = aue.txHash " +
                    "WHERE aue.ownerAddr = :stakeAddress " +
                    "ORDER BY d.slot DESC "
    )
    List<DRepRegistrationEntity> findDRepByRegistrationTxOwnerAddress(
            String stakeAddress,
            Pageable pageable
    );

}
