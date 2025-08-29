package gov.govcircle.common.indexer.dbsync.service;


import gov.govcircle.common.indexer.dbsync.model.entity.StakeAddress;

import java.util.Optional;

public interface StakeAddressService {
    Optional<StakeAddress> findById(Long id);


}
