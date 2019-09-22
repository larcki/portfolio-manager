package com.nordcomet.pflio.repo;

import com.nordcomet.pflio.model.Asset;
import com.nordcomet.pflio.model.Tags;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface AssetRepo extends CrudRepository<Asset, Integer> {

    Set<Asset> findAssetsByTagsNameIn(List<Tags> tags);

}
