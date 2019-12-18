package com.nordcomet.pflio.asset.repo;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.AssetClassType;
import com.nordcomet.pflio.asset.model.Region;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AssetRepo extends CrudRepository<Asset, Integer> {

    Set<Asset> findAssetsByParserOptionsNotNull();

    Optional<Asset> findAssetsById(Integer assetId);

    Set<Asset> findAll();

    Optional<Asset> findAssetByIsin(String isin);

    List<Asset> findAssetsByAssetClasses2AssetClassTypeInAndAssetClasses2RegionIn(List<AssetClassType> assetClassTypes, List<Region> regions);

    List<Asset> findAssetsByAssetClasses2AssetClassTypeIn(List<AssetClassType> assetClassTypes);

    List<Asset> findAssetsByAssetClasses2RegionIn(List<Region> regions);

}
