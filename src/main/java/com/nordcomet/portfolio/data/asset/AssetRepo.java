package com.nordcomet.portfolio.data.asset;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AssetRepo extends CrudRepository<Asset, Integer> {

    Set<Asset> findAssetsByParserOptionsNotNull();

    Optional<Asset> findAssetsById(Integer assetId);

    Set<Asset> findAll();

    Optional<Asset> findAssetByIsin(String isin);

    Set<Asset> findAssetsByAssetClassesAssetClassTypeInAndAssetClassesRegionIn(List<AssetClassType> assetClassTypes, List<Region> regions);

    Set<Asset> findAssetsByAssetClassesAssetClassTypeIn(List<AssetClassType> assetClassTypes);

    Set<Asset> findAssetsByAssetClassesRegionIn(List<Region> regions);

}
