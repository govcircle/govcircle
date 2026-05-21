package gov.govcircle.common.repository.utxo;

import com.bloxbean.cardano.yaci.store.staking.storage.impl.model.PoolRegistrationEnity;
import com.bloxbean.cardano.yaci.store.utxo.storage.impl.model.AddressUtxoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class AddressUtxoStorageExtendedImpl implements AddressUtxoStorageExtended {
//    @PersistenceContext
//    private EntityManager entityManager;

    @Override
    public List<AddressUtxoEntity> findAddressUtxoByStakeAddress(
            String stakeAddress,
            Pageable pageable
    ) {
//        return entityManager
//                .createQuery(
//                        "SELECT a FROM AddressUtxoEntity a " +
//                                "WHERE a.ownerStakeAddr = :stakeAddress " +
//                                "ORDER BY a.slot DESC ",
//                        AddressUtxoEntity.class
//                )
//                .getResultList();
        return Collections.emptyList();

    }

}
