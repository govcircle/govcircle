package gov.govcircle.common.repository.utxo;

import com.bloxbean.cardano.yaci.store.utxo.storage.impl.model.AddressUtxoEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddressUtxoStorageExtended {
    List<AddressUtxoEntity> findAddressUtxoByStakeAddress(
            String stakeAddress,
            Pageable pageable
    );

}
