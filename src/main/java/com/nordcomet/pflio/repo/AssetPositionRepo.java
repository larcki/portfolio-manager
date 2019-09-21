package com.nordcomet.pflio.repo;

import com.nordcomet.pflio.model.snapshot.AssetPosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AssetPositionRepo extends CrudRepository<AssetPosition, Integer> {

//    @Query("SELECT ap from asset_position ap WHERE ap.asset_id = ?1 ORDER BY timestamp DESC LIMIT 1")
//    AssetPosition findLatestByAssetId(Integer assetId);
    Optional<AssetPosition> findFirstByAssetIdOrderByTimestampDesc(Integer assetId);
    Optional<AssetPosition> findFirstByAssetIdAndTimestampBefore(Integer assetId, LocalDateTime timestamp);

    List<AssetPosition> findAllByAssetIdAndTimestampAfter(Integer assetId, LocalDateTime timestamp);
}
