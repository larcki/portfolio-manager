package com.nordcomet.portfolio.data.assetposition;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AssetPositionRepo extends CrudRepository<AssetPosition, Integer> {

    Optional<AssetPosition> findFirstByAssetIdOrderByTimestampDesc(Integer assetId);
    List<AssetPosition> findAllByAssetIdAndTimestampAfter(Integer assetId, LocalDateTime timestamp);
    Optional<AssetPosition> findFirstByAssetIdAndTimestampBeforeOrderByTimestampDesc(Integer assetId, LocalDateTime timestamp);
    List<AssetPosition> findAllByAssetId(Integer assetId);
    List<AssetPosition> findAllByAssetIdInOrderByTimestampDesc(List<Integer> assets);

}
