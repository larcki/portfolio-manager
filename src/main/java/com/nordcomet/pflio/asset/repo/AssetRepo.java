package com.nordcomet.pflio.asset.repo;

import com.nordcomet.pflio.asset.model.Asset;
import com.nordcomet.pflio.asset.model.Tags;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AssetRepo extends CrudRepository<Asset, Integer> {

    Set<Asset> findAssetsByTagsNameIn(List<Tags> tags);

    Set<Asset> findAssetsByParserOptionsNotNull();

    Optional<Asset> findAssetsById(Integer assetId);

}
