package com.nordcomet.pflio.asset.repo;

import com.nordcomet.pflio.asset.model.AssetPosition;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AssetPositionRepo extends CrudRepository<AssetPosition, Integer> {

    Optional<AssetPosition> findFirstByAssetIdOrderByTimestampDesc(Integer assetId);
    List<AssetPosition> findAllByAssetIdAndTimestampAfter(Integer assetId, LocalDateTime timestamp);
    Optional<AssetPosition> findFirstByAssetIdAndTimestampBeforeOrderByTimestampDesc(Integer assetId, LocalDateTime timestamp);
    List<AssetPosition> findAllByAssetId(Integer assetId);

}
