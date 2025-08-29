package gov.govcircle.common.indexer.dbsync.repository;

import gov.govcircle.common.indexer.dbsync.model.entity.StakeAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StakeAddressRepository extends JpaRepository<StakeAddress, Long> {

    @Override
    @Query("SELECT sa FROM StakeAddress sa WHERE sa.id = :id")
    Optional<StakeAddress> findById(@Param("id") Long id);
}
